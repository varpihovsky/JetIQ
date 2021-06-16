package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkbookSubjectDTO(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val credits: String,
    val date: String,
    val ects: String,
    val form: String,
    val hours: String,
    val mark: String,
    val subj_name: String,
    val teacher: String,
    val total: Int,
    val semester: Int = 0
) {
    fun withSemester(semester: Int) = MarkbookSubjectDTO(
        id,
        credits,
        date,
        ects,
        form,
        hours,
        mark,
        subj_name,
        teacher,
        total,
        semester
    )

    fun withId(id: Int) = MarkbookSubjectDTO(
        id, credits, date, ects, form, hours, mark, subj_name, teacher, total, semester
    )
}


