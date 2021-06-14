package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.SubjectDetailsDAO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.SubjectTaskDTO
import com.varpihovsky.jetiq.back.dto.relations.SubjectDetailsWithTasks
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class SubjectDetailsDatabaseManager @Inject constructor(
    private val subjectDetailsDAO: SubjectDetailsDAO
) {
    fun addDetails(subjectDetailsDTO: SubjectDetailsDTO) {
        subjectDetailsDAO.insertDetails(subjectDetailsDTO)
    }

    fun add(subjectDetailsWithTasks: SubjectDetailsWithTasks) {
        subjectDetailsDAO.insertDetails(subjectDetailsWithTasks.subjectDetailsDTO)
        subjectDetailsWithTasks.subjectTasks.forEach {
            subjectDetailsDAO.insertTask(it.withId(subjectDetailsWithTasks.subjectDetailsDTO.id))
        }
    }

    fun getAllDetails() = subjectDetailsDAO.getDetailsOnly()

    fun getDetailsWithTasks() = subjectDetailsDAO.getDetailsWithTasks().distinctUntilChanged()

    fun getDetailsById(id: Int) = subjectDetailsDAO.getDetailsById(id).distinctUntilChanged()

    fun getDetailsAndTasksByDetailsId(id: Int) =
        subjectDetailsDAO.getDetailsAndTasksByDetailsId(id).distinctUntilChanged()

    fun deleteDetailsWithTasks(detailsWithTasks: SubjectDetailsWithTasks) {
        subjectDetailsDAO.deleteDetails(detailsWithTasks.subjectDetailsDTO)
        detailsWithTasks.subjectTasks.forEach { subjectDetailsDAO.deleteTask(it) }
    }

    fun deleteDetails(detailsDTO: SubjectDetailsDTO) {
        subjectDetailsDAO.deleteDetails(detailsDTO)
    }

    fun deleteTask(taskDTO: SubjectTaskDTO) {
        subjectDetailsDAO.deleteTask(taskDTO)
    }
}