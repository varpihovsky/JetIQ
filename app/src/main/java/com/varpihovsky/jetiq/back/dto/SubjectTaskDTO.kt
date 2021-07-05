package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectTaskDTO(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val subjectDetailsId: Int,
    val legend: String,
    val num_mod: String,
    val points: Int,
) {
    fun withId(id: Int) =
        SubjectTaskDTO(id, subjectDetailsId, legend, num_mod, points)
}

