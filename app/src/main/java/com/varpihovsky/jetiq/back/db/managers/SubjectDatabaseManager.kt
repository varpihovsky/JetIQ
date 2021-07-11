package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.SubjectDAO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import javax.inject.Inject

class SubjectDatabaseManager @Inject constructor(
    private val subjectDAO: SubjectDAO
) {
    fun add(subjectDTO: SubjectDTO) {
        subjectDAO.insert(subjectDTO)
    }

    fun getAll() = subjectDAO.getAllSubjects()

    fun getAllList() = subjectDAO.getAllSubjectsList()

    fun delete(subjectDTO: SubjectDTO) {
        subjectDAO.delete(subjectDTO)
    }

    fun deleteById(id: Int) {
        subjectDAO.deleteSubjectById(id)
    }

    fun deleteAll() = subjectDAO.deleteAll()
}