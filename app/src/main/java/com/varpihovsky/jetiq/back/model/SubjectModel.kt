package com.varpihovsky.jetiq.back.model

import android.util.Log
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
import com.varpihovsky.jetiq.system.util.ReactiveTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.last
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

    private val scope = CoroutineScope(Dispatchers.IO)

    private val subjectDetailsTask = ReactiveTask(task = this::addSubjectDetails)
    private val markbookSubjectsTask = ReactiveTask(task = this::addMarkbookSubjects)

    fun getSubjectList(): Flow<List<SubjectDTO>> {
        areSubjectsLoading = true
        addSuccessJournal()
        return subjectDatabaseManager.getAll()
    }

    private fun addSuccessJournal() {
        val session = requireSession()
        jetIQSubjectManager.getSuccessJournal(session).forEach {
            Log.d(DEBUG_PREFIX, "Added SubjectDTO $it")
            subjectDatabaseManager.add(it)
        }
        areSubjectsLoading = false
        updateLoadingState()
    }

    fun getSubjectById(id: Int) = subjectDatabaseManager.getById(id)

    fun removeSubject(subjectDTO: SubjectDTO) = subjectDatabaseManager.delete(subjectDTO)

    fun removeSubjectById(id: Int) = subjectDatabaseManager.getById(id)

    fun getSubjectDetailsList(): Flow<List<SubjectDetailsDTO>> {
        areDetailsLoading = true
        subjectDetailsTask.start()
        return subjectDetailsDatabaseManager.getAllDetails()
    }

    private suspend fun addSubjectDetails() {
        val session = requireSession()

        subjectDatabaseManager.getAll().filter { it.isNotEmpty() }.collect { list ->
            Log.d(DEBUG_PREFIX, "Received SubjectDTO list with size ${list.size}")
            list.forEach { subject ->
                jetIQSubjectManager.getSubjectDetails(
                    session,
                    subject.card_id.toInt()
                ).let {
                    val details = SubjectDetailsWithTasks(
                        it.subjectDetailsDTO.withId(subject.card_id.toInt()),
                        it.subjectTasks
                    )
                    Log.d(DEBUG_PREFIX, "Added SubjectDetails $details")
                    subjectDetailsDatabaseManager.add(details)
                }
            }
            if (list.size == subjectDetailsDatabaseManager.getAllDetails().last().size) {
                areDetailsLoading = false
                updateLoadingState()
            }
        }
    }

    fun getMarkbookSubjects(): Flow<List<MarkbookSubjectDTO>> {
        markbookSubjectsTask.start()
        return subjectDetailsDatabaseManager.getMarkbookSubjects()
    }

    private suspend fun addMarkbookSubjects() {
        val session = requireSession()
        subjectDatabaseManager.getAll().collect { subjects ->
            jetIQSubjectManager.getMarkbookSubjects(session)
                .let { markbookSubjects ->
                    subjects.forEach { subject ->
                        markbookSubjects.find {
                            subject.t_name == it.teacher &&
                                    subject.subject == it.subj_name &&
                                    subject.sem.toInt() == it.semester
                        }?.let {
                            subjectDetailsDatabaseManager.addMarkbookSubject(it.withId(subject.card_id.toInt()))
                        }
                    }
                }
        }
    }

    private fun updateLoadingState() {
        scope.launch(Dispatchers.Main) {
            isLoading.value = areSubjectsLoading && areDetailsLoading
        }
    }

    fun removeAllSubjects() {
        areSubjectsLoading = true
        updateLoadingState()

        subjectDetailsTask.stop()
        markbookSubjectsTask.stop()

        subjectDatabaseManager.deleteAll()
        subjectDetailsDatabaseManager.deleteAllDetails()
        subjectDetailsDatabaseManager.deleteAllTasks()
        subjectDetailsDatabaseManager.deleteAllMarkbookSubjects()
    }

    companion object {
        private const val DEBUG_PREFIX = "SubjectModel"
    }
}