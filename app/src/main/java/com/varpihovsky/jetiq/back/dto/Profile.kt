package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity

@Entity
data class Profile(
    val course_num: Int,
    val d_id: String,
    val d_name: String,
    val dob: String,
    val email: String,
    val f_id: String,
    val gr_id: String,
    val gr_name: String,
    val id: String,
    val photo_url: String,
    val session: String,
    val spec_id: String,
    val u_name: String
)