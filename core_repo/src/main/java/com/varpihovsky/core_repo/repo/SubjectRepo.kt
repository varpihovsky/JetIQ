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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SubjectRepo : Refreshable {
    fun loadSuccessJournal()
    fun loadMarkbookSubjects()

    fun getSubjects(): Flow<List<SubjectDTO>>
    fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>>
    fun getMarkbook(): Flow<List<MarkbookSubjectDTO>>

    fun clear()

    companion object {
        internal operator fun invoke(
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
        TODO("Not yet implemented")
    }

    override fun getSubjects(): Flow<List<SubjectDTO>> {
        return subjectDAO.getAllSubjects()
    }

    override fun loadSuccessJournal() {
        toggleLoading { areSubjectsLoading = true }

        jetIQSubjectManager.getSuccessJournal(requireSession()).forEach {
            subjectDAO.insert(it)
        }

        addSubjectDetails()

        toggleLoading { areSubjectsLoading = false }
    }

    override fun getSubjectsDetails(): Flow<List<SubjectDetailsDTO>> {
        return subjectDetailsDAO.getDetailsOnly()
    }

    private fun addSubjectDetails() {
        subjectDAO.getAllSubjectsList().forEach { subject ->
            jetIQSubjectManager.getSubjectDetails(
                requireSession(),
                subject.card_id.toInt()
            ).let { subjectDetailsWithTasks ->
                subjectDetailsDAO.insertDetails(subjectDetailsWithTasks.subjectDetailsDTO.copy(id = subject.card_id.toInt()))
                subjectDetailsWithTasks.subjectTasks.forEach { subjectDetailsDAO.insertTask(it) }
            }
        }
    }


    override fun getMarkbook(): Flow<List<MarkbookSubjectDTO>> {
        return subjectDetailsDAO.getMarkbookSubjects()
    }

    override fun loadMarkbookSubjects() {
        toggleLoading { areMarkbookSubjectsLoading = true }
        addMarkbookSubjects()
        toggleLoading { areMarkbookSubjectsLoading = false }
    }

    private fun addMarkbookSubjects() {
        val session = requireSession()
        subjectDAO.getAllSubjectsList().let { subjects ->
            jetIQSubjectManager.getMarkbookSubjects(session)
                .let { markbookSubjects ->
                    subjects.forEach { subject ->
                        markbookSubjects.find {
                            subject.t_name == it.teacher &&
                                    subject.subject == it.subj_name &&
                                    subject.sem.toInt() == it.semester
                        }?.let {
                            subjectDetailsDAO.insertMarkbookSubject(it.copy(id = subject.card_id.toInt()))
                        }
                    }
                    areMarkbookSubjectsLoading = false
                    updateLoadingState()
                }
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