package com.varpihovsky.core_repo.repo

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
     * Returns flow of subject details from subject provided by success journal.
     * Every detail has same id as [SubjectDTO] from [getSubjects] method. Always sorted same as
     * result from [getSubjects] method.
     *
     * @return list of [SubjectDetailsDTO]
     */
    fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>>

    /**
     * Returns flow of markbook subjects.
     *
     * @return list of [MarkbookSubjectDTO]
     */
    fun getMarkbook(): Flow<List<MarkbookSubjectDTO>>

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

    override fun onRefresh() {
        modelScope.launch(Dispatchers.IO) {
            load()
        }
    }

    override suspend fun load() {
        modelScope.launch {
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
        subjectDetailsWithTasks.subjectTasks.forEach { subjectDetailsDAO.insertTask(it) }
    }

    override fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>> {
        return subjectDetailsDAO.getDetailsOnly()
    }

    override fun getMarkbook(): Flow<List<MarkbookSubjectDTO>> {
        return subjectDetailsDAO.getMarkbookSubjects()
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