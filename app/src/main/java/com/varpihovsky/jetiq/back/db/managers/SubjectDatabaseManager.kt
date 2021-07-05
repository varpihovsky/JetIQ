package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.SubjectDAO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class SubjectDatabaseManager @Inject constructor(
    private val subjectDAO: SubjectDAO
) {
    fun add(subjectDTO: SubjectDTO) {
        subjectDAO.insert(subjectDTO)
    }

    fun getById(id: Int) = subjectDAO.getSubjectById(id).distinctUntilChanged()

    fun getAll() = subjectDAO.getAllSubjects()

    fun delete(subjectDTO: SubjectDTO) {
        subjectDAO.delete(subjectDTO)
    }

    fun deleteById(id: Int) {
        subjectDAO.deleteSubjectById(id)
    }

    fun deleteAll() = subjectDAO.deleteAll()
}