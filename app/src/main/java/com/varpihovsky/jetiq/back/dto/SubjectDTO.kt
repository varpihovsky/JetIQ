package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO

@Entity
data class SubjectDTO(
    @PrimaryKey(autoGenerate = false) val card_id: String,
    val f_control: String,
    val scale: String,
    val sem: String,
    val subject: String,
    val t_name: String
) {
    fun toUIDTO(total: Int) = UISubjectDTO(
        card_id.toInt(),
        subject,
        t_name,
        total,
        sem.toInt()
    )
}

