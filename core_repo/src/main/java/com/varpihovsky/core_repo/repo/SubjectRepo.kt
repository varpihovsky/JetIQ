package com.varpihovsky.core_repo.repo

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.SubjectDAO
import com.varpihovsky.core_db.dao.SubjectDetailsDAO
import com.varpihovsky.core_network.managers.JetIQSubjectManager
import com.varpihovsky.repo_data.MarkbookSubjectDTO
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Interface used for providing subject data of current user.
 *
 * @author Vladyslav Podrezenko
 */
interface SubjectRepo : Refreshable {
    /**
     * Loads all data about subjects from success journal and markbook and stores them in database.
     * Ignores all new changes.
     */
    suspend fun load()

    /**
     * Returns flow of current saved subjects provided by success journal.
     *
     * @return list of [SubjectDTO]
     */
    fun getSubjects(): Flow<List<SubjectDTO>>

    /**
     * Returns flow of subject with specified id.
     *
     * @param id id of [SubjectDTO]
     *
     * @return [SubjectDTO]
     */
    fun getSubjectById(id: Int): Flow<SubjectDTO>

    /**
     * Returns flow of subject details from subject provided by success journal.
     * Every detail has same id as [SubjectDTO] from [getSubjects] method. Always sorted same as
     * result from [getSubjects] method.
     *
     * @return list of [SubjectDetailsDTO]
     */
    fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>>

    /**
     * Returns flow of subject details with specified id.
     *
     * @param id id of requested [SubjectDetailsWithTasks]
     *
     * @return [SubjectDetailsWithTasks]
     */
    fun getDetailsById(id: Int): Flow<SubjectDetailsWithTasks>

    /**
     * Returns flow of markbook subjects.
     *
     * @return list of [MarkbookSubjectDTO]
     */
    fun getMarkbook(): Flow<List<MarkbookSubjectDTO>>

    /**
     * Returns flow of markbook subject related to id.
     *
     * @param id id of [MarkbookSubjectDTO]
     *
     * @return [MarkbookSubjectDTO]
     */
    fun getMarkbookById(id: Int): Flow<MarkbookSubjectDTO>

    /**
     * Clears database.
     */
    fun clear()

    companion object {
        operator fun invoke(
            subjectDAO: SubjectDAO,
            subjectDetailsDAO: SubjectDetailsDAO,
            jetIQSubjectManager: JetIQSubjectManager,
            confidentialDAO: ConfidentialDAO,
            profileDAO: ProfileDAO,
            exceptionEventManager: ExceptionEventManager,
            profileRepo: ProfileRepo
        ): SubjectRepo = SubjectRepoImpl(
            subjectDAO,
            subjectDetailsDAO,
            jetIQSubjectManager,
            confidentialDAO,
            profileDAO,
            exceptionEventManager,
            profileRepo
        )
    }
}

private class SubjectRepoImpl @Inject constructor(
    private val subjectDAO: SubjectDAO,
    private val subjectDetailsDAO: SubjectDetailsDAO,
    private val jetIQSubjectManager: JetIQSubjectManager,
    confidentialDAO: ConfidentialDAO,
    profileDAO: ProfileDAO,
    exceptionEventManager: ExceptionEventManager,
    private val profileRepo: ProfileRepo
) : ConfidentRepo(confidentialDAO, profileDAO, exceptionEventManager), SubjectRepo {
    override val isLoading = mutableStateOf(false)

    private var taskIndex = 0

    override fun onRefresh() {
        repoScope.launch(Dispatchers.IO) {
            load()
        }
    }

    override suspend fun load() {
        repoScope.launch {
            isLoading.value = true

            val successJob = launch { loadSuccessJournal() }
            val markbookJob = launch { loadMarkbookSubjects() }

            successJob.join()
            markbookJob.join()

            isLoading.value = false
        }.join()
    }

    override fun getSubjects(): Flow<List<SubjectDTO>> {
        return subjectDAO.getAllSubjects()
    }

    override fun getSubjectById(id: Int): Flow<SubjectDTO> {
        return subjectDAO.getSubjectById(id.toString()).distinctUntilChanged()
    }

    private suspend fun loadSuccessJournal() {
        val session = requireSession()
        wrapException(
            result = jetIQSubjectManager.getSuccessJournal(session),
            onSuccess = {
                if (it.value.isNotEmpty()) {
                    processSubjects(it.value, session)
                    return@wrapException
                }

                val confidential = requireConfidential()
                profileRepo.login(confidential.login, confidential.password)
                loadSuccessJournal()
            }
        )
    }

    private suspend fun processSubjects(subjects: List<SubjectDTO>, session: String) {
        taskIndex = 0
        subjects.forEach {
            subjectDAO.insert(it)
            addSubjectDetails(session, it.card_id.toInt())
        }
    }

    private suspend fun addSubjectDetails(session: String, id: Int) {
        wrapException(
            result = jetIQSubjectManager.getSubjectDetails(session, id),
            onSuccess = { processSubjectDetails(it.value, id) }
        )
    }

    private fun processSubjectDetails(subjectDetailsWithTasks: SubjectDetailsWithTasks, id: Int) {
        subjectDetailsDAO.insertDetails(subjectDetailsWithTasks.subjectDetailsDTO.copy(id = id))
        subjectDetailsWithTasks.subjectTasks.forEach { task ->
            subjectDetailsDAO.insertTask(
                task.copy(subjectDetailsId = id, id = taskIndex)
            )
            taskIndex++
        }
    }

    override fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>> {
        return subjectDetailsDAO.getDetailsOnly()
    }

    override fun getDetailsById(id: Int): Flow<SubjectDetailsWithTasks> {
        return subjectDetailsDAO.getDetailsById(id).distinctUntilChanged()
    }

    override fun getMarkbook(): Flow<List<MarkbookSubjectDTO>> {
        return subjectDetailsDAO.getMarkbookSubjects()
    }

    override fun getMarkbookById(id: Int): Flow<MarkbookSubjectDTO> {
        return subjectDetailsDAO.getMarkbookSubjectById(id).distinctUntilChanged()
    }

    private suspend fun loadMarkbookSubjects() {
        val session = requireSession()
        wrapException(
            result = jetIQSubjectManager.getMarkbookSubjects(session),
            onSuccess = { processMarkbook(it.value) }
        )
    }

    private fun processMarkbook(markbookSubjects: List<MarkbookSubjectDTO>) {
        val current = subjectDetailsDAO.getMarkbookSubjectsList().map { it.copy(id = 0) }
        markbookSubjects.forEach {
            if (!current.contains(it)) {
                subjectDetailsDAO.insertMarkbookSubject(it)
            }
        }
    }

    override fun clear() {
        clearDatabases()
    }

    private fun clearDatabases() {
        subjectDAO.deleteAll()
        subjectDetailsDAO.deleteAllDetails()
        subjectDetailsDAO.deleteAllTasks()
        subjectDetailsDAO.deleteAllMarkbookSubjects()
    }
}