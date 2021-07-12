package com.varpihovsky.repo_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDTO(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val text: String,
    val type: String
) {
    companion object {
        const val TYPE_STUDENT = "student"
        const val TYPE_TEACHER = "teacher"
    }
}