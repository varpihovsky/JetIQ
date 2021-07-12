package com.varpihovsky.repo_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectTaskDTO(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val subjectDetailsId: Int,
    val legend: String,
    val num_mod: String,
    val points: Int,
)
