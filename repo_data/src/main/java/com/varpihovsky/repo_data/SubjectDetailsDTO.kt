package com.varpihovsky.repo_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectDetailsDTO(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val ects: String,
    val for_pres1: Int,
    val for_pres2: Int,
    val h_pres1: Int,
    val h_pres2: Int,
    val mark1: Int,
    val mark2: Int,
    val sum1: Int,
    val sum2: Int,
    val total: Int,
    val total_prev: Int
)

