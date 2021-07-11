package com.varpihovsky.jetiq.back.model

import androidx.lifecycle.MutableLiveData
import com.varpihovsky.jetiq.back.api.managers.JetIQSubjectManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.SubjectDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.SubjectDetailsDatabaseManager
import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubjectModel @Inject constructor(
    confidentialDatabaseManager: ConfidentialDatabaseManager,
    private val subjectDatabaseManager: SubjectDatabaseManager,
    private val subjectDetailsDatabaseManager: SubjectDetailsDatabaseManager,
    private val jetIQSubjectManager: JetIQSubjectManager,
    profileDatabaseManager: ProfileDatabaseManager
) : ConfidentModel(confidentialDatabaseManager, profileDatabaseManager) {
    val isLoading = MutableLiveData(false)

    private var areSubjectsLoading = false
    private var areDetailsLoading = false
    private var areMarkbookSubjectsLoading = false

    fun getSubjectList(): Flow<List<SubjectDTO>> {
        return subjectDatabaseManager.getAll()
    }

    fun loadSuccessJournal() {
        toggleLoading { areSubjectsLoading = true }

        jetIQSubjectManager.getSuccessJournal(requireSession()).forEach {
            subjectDatabaseManager.add(it)
        }

        addSubjectDetails()

        toggleLoading { areSubjectsLoading = false }
    }

    fun getSubjectDetailsList(): Flow<List<SubjectDetailsDTO>> {
        return subjectDetailsDatabaseManager.getAllDetails()
    }

    private fun addSubjectDetails() {
        subjectDatabaseManager.getAllList().forEach { subject ->
            jetIQSubjectManager.getSubjectDetails(
                requireSession(),
                subject.card_id.toInt()
            ).let {
                val details = SubjectDetailsWithTasks(
                    it.subjectDetailsDTO.copy(id = subject.card_id.toInt()),
                    it.subjectTasks
                )
                subjectDetailsDatabaseManager.add(details)
            }
        }
    }


    fun getMarkbookSubjects(): Flow<List<MarkbookSubjectDTO>> {
        return subjectDetailsDatabaseManager.getMarkbookSubjects()
    }

    fun loadMarkbookSubjects() {
        toggleLoading { areMarkbookSubjectsLoading = true }
        addMarkbookSubjects()
        toggleLoading { areMarkbookSubjectsLoading = false }
    }

    private fun addMarkbookSubjects() {
        val session = requireSession()
        subjectDatabaseManager.getAllList().let { subjects ->
            jetIQSubjectManager.getMarkbookSubjects(session)
                .let { markbookSubjects ->
                    subjects.forEach { subject ->
                        markbookSubjects.find {
                            subject.t_name == it.teacher &&
                                    subject.subject == it.subj_name &&
                                    subject.sem.toInt() == it.semester
                        }?.let {
                            subjectDetailsDatabaseManager.addMarkbookSubject(it.copy(id = subject.card_id.toInt()))
                        }
                    }
                    areMarkbookSubjectsLoading = false
                    updateLoadingState()
                }
        }
    }

    fun removeAllSubjects() {
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
        subjectDatabaseManager.deleteAll()
        subjectDetailsDatabaseManager.deleteAllDetails()
        subjectDetailsDatabaseManager.deleteAllTasks()
        subjectDetailsDatabaseManager.deleteAllMarkbookSubjects()
    }
}