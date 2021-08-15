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
package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.log.e
import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.core_db.dao.SubjectDAO
import com.varpihovsky.core_db.dao.SubjectDetailsDAO
import com.varpihovsky.core_repo.apiMappers.withID
import com.varpihovsky.core_repo.apiMappers.withTasks
import com.varpihovsky.jetiqApi.Api
import com.varpihovsky.jetiqApi.data.MarkbookSubject
import com.varpihovsky.jetiqApi.data.Subject
import com.varpihovsky.jetiqApi.data.SubjectDetails
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

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
     * @return list of [Subject]
     */
    fun getSubjects(): Flow<List<Subject>>

    /**
     * Returns flow of subject with specified id.
     *
     * @param id id of [Subject]
     *
     * @return [Subject]
     */
    fun getSubjectById(id: Int): Flow<Subject?>

    /**
     * Returns flow of subject details from subject provided by success journal.
     * Every detail has same id as [Subject] from [getSubjects] method. Always sorted same as
     * result from [getSubjects] method.
     *
     * @return list of [SubjectDetails]
     */
    fun getSubjectsDetails(): Flow<List<SubjectDetails>>

    /**
     * Returns flow of subject details with specified id.
     *
     * @param id id of requested [SubjectDetails]
     *
     * @return [SubjectDetails]
     */
    fun getDetailsById(id: Int): Flow<SubjectDetails?>

    /**
     * Returns flow of markbook subjects.
     *
     * @return list of [MarkbookSubject]
     */
    fun getMarkbook(): Flow<List<MarkbookSubject>>

    /**
     * Returns flow of markbook subject related to id.
     *
     * @param id id of [MarkbookSubject]
     *
     * @return [MarkbookSubject]
     */
    fun getMarkbookById(id: Int): Flow<MarkbookSubject?>

    /** Clears database. */
    fun clear()

    companion object {
        operator fun invoke(
            subjectDAO: SubjectDAO,
            subjectDetailsDAO: SubjectDetailsDAO,
            api: Api,
            confidentialDAO: ConfidentialDAO,
            profileDAO: ProfileDAO,
            exceptionEventManager: ExceptionEventManager,
            profileRepo: ProfileRepo
        ): SubjectRepo = SubjectRepoImpl(
            subjectDAO,
            subjectDetailsDAO,
            api,
            confidentialDAO,
            profileDAO,
            exceptionEventManager,
            profileRepo
        )
    }
}

private class SubjectRepoImpl constructor(
    private val subjectDAO: SubjectDAO,
    private val subjectDetailsDAO: SubjectDetailsDAO,
    private val api: Api,
    confidentialDAO: ConfidentialDAO,
    profileDAO: ProfileDAO,
    exceptionEventManager: ExceptionEventManager,
    private val profileRepo: ProfileRepo
) : ConfidentRepo(confidentialDAO, profileDAO, exceptionEventManager), SubjectRepo {
    override val isLoading = mutableStateOf(false)

    private var taskIndex = 0

    override suspend fun onRefresh() {
        load()
    }

    override suspend fun load() {
        if (currentSession() == null) return

        isLoading.value = true

        try {
            launchAllJobsWithTimeout()
        } catch (e: TimeoutCancellationException) {
            e(e.stackTraceToString())
        }

        isLoading.value = false
    }

    private suspend fun launchAllJobsWithTimeout() = withTimeout(JOB_TIMEOUT) {
        val successJob = launch { loadSuccessJournal() }
        val markbookJob = launch { loadMarkbookSubjects() }

        successJob.join()
        markbookJob.join()
    }


    override fun getSubjects(): Flow<List<Subject>> {
        return subjectDAO.getAllSubjects()
    }

    override fun getSubjectById(id: Int): Flow<Subject?> {
        return subjectDAO.getSubjectById(id.toString()).distinctUntilChanged()
    }

    private suspend fun loadSuccessJournal() {
        val session = requireSession()
        wrapException(
            result = api.getSuccessJournal(session),
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

    private suspend fun processSubjects(subjects: List<Subject>, session: String) {
        taskIndex = 0
        subjects.forEach {
            subjectDAO.insert(it)
            addSubjectDetails(session, it.subjectId.toInt())
        }
    }

    private suspend fun addSubjectDetails(session: String, id: Int) {
        wrapException(
            result = api.getSubjectDetails(session, id),
            onSuccess = { processSubjectDetails(it.value, id) }
        )
    }

    private fun processSubjectDetails(subjectDetailsWithTasks: SubjectDetails, id: Int) {
        val tasks = subjectDetailsWithTasks.tasks.map { it.withID(taskIndex++) }
        val details = subjectDetailsWithTasks.withTasks(tasks)
        subjectDetailsDAO.insertDetails(details.withID(id))
    }

    override fun getSubjectsDetails(): Flow<List<SubjectDetails>> {
        return subjectDetailsDAO.getDetails()
    }

    override fun getDetailsById(id: Int): Flow<SubjectDetails?> {
        return subjectDetailsDAO.getDetailsById(id).distinctUntilChanged()
    }

    override fun getMarkbook(): Flow<List<MarkbookSubject>> {
        return subjectDetailsDAO.getMarkbookSubjects()
    }

    override fun getMarkbookById(id: Int): Flow<MarkbookSubject?> {
        return subjectDetailsDAO.getMarkbookSubjectById(id).distinctUntilChanged()
    }

    private suspend fun loadMarkbookSubjects() {
        val session = requireSession()
        wrapException(
            result = api.getMarkbookSubjects(session),
            onSuccess = { success -> processMarkbook(success.value) }
        )
    }

    private fun processMarkbook(markbookSubjects: List<MarkbookSubject>) {
        markbookSubjects.forEachIndexed { index, it ->
            subjectDetailsDAO.insertMarkbookSubject(it.withID(index))
        }
    }

    override fun clear() {
        clearDatabases()
    }

    private fun clearDatabases() {
        subjectDAO.deleteAll()
        subjectDetailsDAO.deleteAllDetails()
        subjectDetailsDAO.deleteAllMarkbookSubjects()
    }

    companion object {
        private const val JOB_TIMEOUT = 15000L
    }
}