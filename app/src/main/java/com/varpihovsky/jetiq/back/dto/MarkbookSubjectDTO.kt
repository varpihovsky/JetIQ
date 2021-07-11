package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO

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
    fun toUIDTO() = UISubjectDTO(
        id,
        subj_name,
        teacher,
        total,
        semester
    )
}


