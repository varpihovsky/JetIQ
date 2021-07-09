package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varpihovsky.jetiq.ui.dto.ReceiverType
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO

@Entity
data class ContactDTO(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val text: String,
    val type: String
) {
    fun toUIDTO() = UIReceiverDTO(
        id,
        text,
        when (type) {
            TYPE_STUDENT -> ReceiverType.STUDENT
            TYPE_TEACHER -> ReceiverType.TEACHER
            else -> throw RuntimeException()
        }
    )

    companion object {
        const val TYPE_STUDENT = "student"
        const val TYPE_TEACHER = "teacher"
    }
}