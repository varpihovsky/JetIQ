package com.varpihovsky.core_repo.repo

import com.varpihovsky.core.Refreshable
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

interface SubjectRepo : Refreshable {
    suspend fun loadSuccessJournal()
    suspend fun loadMarkbookSubjects()

    fun getSubjects(): Flow<List<SubjectDTO>>
    fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>>
    fun getMarkbook(): Flow<List<MarkbookSubjectDTO>>

    fun clear()

    companion object {
        operator fun invoke(
            subjectDAO: SubjectDAO,
            subjectDetailsDAO: SubjectDetailsDAO,
            jetIQSubjectManager: JetIQSubjectManager,
            confidentialDAO: ConfidentialDAO,
            profileDAO: ProfileDAO,
        ): SubjectRepo = SubjectRepoImpl(
            subjectDAO,
            subjectDetailsDAO,
            jetIQSubjectManager,
            confidentialDAO,
            profileDAO
        )
    }
}

private class SubjectRepoImpl @Inject constructor(
    private val subjectDAO: SubjectDAO,
    private val subjectDetailsDAO: SubjectDetailsDAO,
    private val jetIQSubjectManager: JetIQSubjectManager,
    confidentialDAO: ConfidentialDAO,
    profileDAO: ProfileDAO,
) : ConfidentRepo(confidentialDAO, profileDAO), SubjectRepo {
    override val isLoading = mutableStateOf(false)

    private var areSubjectsLoading = false
    private var areDetailsLoading = false
    private var areMarkbookSubjectsLoading = false

    override fun onRefresh() {
        modelScope.launch(Dispatchers.IO) {
            loadSuccessJournal()
            loadMarkbookSubjects()
        }
    }

    override fun getSubjects(): Flow<List<SubjectDTO>> {
        return subjectDAO.getAllSubjects()
    }

    override suspend fun loadSuccessJournal() {
        toggleLoading { areSubjectsLoading = true }

        val session = requireSession()

        wrapException(
            result = jetIQSubjectManager.getSuccessJournal(session),
            onSuccess = { processSubjects(it.value, session) },
            onFailure = { toggleLoading { areSubjectsLoading = false } }
        )
    }

    private suspend fun processSubjects(subjects: List<SubjectDTO>, session: String) {
        subjects.forEach {
            subjectDAO.insert(it)
            addSubjectDetails(session, it.card_id.toInt())
        }
        toggleLoading { areSubjectsLoading = false }
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

    override suspend fun loadMarkbookSubjects() {
        toggleLoading { areMarkbookSubjectsLoading = true }
        addMarkbookSubjects()
        toggleLoading { areMarkbookSubjectsLoading = false }
    }

    private suspend fun addMarkbookSubjects() {
        val session = requireSession()
        wrapException(
            result = jetIQSubjectManager.getMarkbookSubjects(session),
            onSuccess = { processMarkbook(it.value) }
        )
    }

    private fun processMarkbook(markbookSubjects: List<MarkbookSubjectDTO>) {
        markbookSubjects.forEach {
            subjectDetailsDAO.insertMarkbookSubject(it)
        }
    }

    override fun clear() {
        toggleLoading { areSubjectsLoading = true }
        clearDatabases()
    }

    private fun toggleLoading(block: () -> Unit) {
        block()
        updateLoadingState()
    }

    private fun updateLoadingState() {
        modelScope.launch(Dispatchers.Main) {
            isLoading.value = areSubjectsLoading && areDetailsLoading
        }
    }

    private fun clearDatabases() {
        subjectDAO.deleteAll()
        subjectDetailsDAO.deleteAllDetails()
        subjectDetailsDAO.deleteAllTasks()
        subjectDetailsDAO.deleteAllMarkbookSubjects()
    }
}