package com.varpihovsky.repo_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectDTO(
    @PrimaryKey(autoGenerate = false) val card_id: String,
    val f_control: String,
    val scale: String,
    val sem: String,
    val subject: String,
    val t_name: String
)

