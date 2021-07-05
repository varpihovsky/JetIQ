package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.managers.JetIQSubjectManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.SubjectDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.SubjectDetailsDatabaseManager
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SubjectModel @Inject constructor(
    confidentialDatabaseManager: ConfidentialDatabaseManager,
    private val subjectDatabaseManager: SubjectDatabaseManager,
    private val subjectDetailsDatabaseManager: SubjectDetailsDatabaseManager,
    private val jetIQSubjectManager: JetIQSubjectManager
) : ConfidentModel(confidentialDatabaseManager) {
    fun getSubjectList(): Flow<List<SubjectDTO>> {
        GlobalScope.launch(Dispatchers.IO) { addSuccessJournal() }
        return subjectDatabaseManager.getAll()
    }

    private fun addSuccessJournal() {
        val confidential = requireConfidential()
        jetIQSubjectManager.getSuccessJournal(confidential.login, confidential.password).forEach {
            subjectDatabaseManager.add(it)
        }
    }

    fun getSubjectById(id: Int) = subjectDatabaseManager.getById(id)

    fun removeSubject(subjectDTO: SubjectDTO) = subjectDatabaseManager.delete(subjectDTO)

    fun removeSubjectById(id: Int) = subjectDatabaseManager.getById(id)

    fun getSubjectDetailsList(): Flow<List<SubjectDetailsDTO>> {
        GlobalScope.launch(Dispatchers.IO) { addSubjectDetails() }
        return subjectDetailsDatabaseManager.getAllDetails()
    }

    private suspend fun addSubjectDetails() {
        val confidential = requireConfidential()
        subjectDatabaseManager.getAll().collect { list ->
            list.forEach { subject ->
                jetIQSubjectManager.getSubjectDetails(
                    confidential.login,
                    confidential.password,
                    subject.card_id.toInt()
                ).let {
                    val details = SubjectDetailsWithTasks(
                        it.subjectDetailsDTO.withId(subject.card_id.toInt()),
                        it.subjectTasks
                    )
                    subjectDetailsDatabaseManager.add(details)
                }
            }
        }
    }
}