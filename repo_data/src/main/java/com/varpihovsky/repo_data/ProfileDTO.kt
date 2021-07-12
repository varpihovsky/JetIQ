package com.varpihovsky.repo_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileDTO(
    val course_num: Int,
    val d_id: String,
    val d_name: String,
    val dob: String?,
    val email: String,
    val f_id: String,
    val gr_id: String,
    val gr_name: String,
    @PrimaryKey(autoGenerate = false) val id: String,
    val photo_url: String,
    val session: String?,
    val spec_id: String,
    val u_name: String
)