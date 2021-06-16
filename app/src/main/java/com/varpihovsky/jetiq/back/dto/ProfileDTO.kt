package com.varpihovsky.jetiq.back.dto

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
) {
    fun withSession(session: String) = ProfileDTO(
        course_num,
        d_id,
        d_name,
        dob,
        email,
        f_id,
        gr_id,
        gr_name,
        id,
        photo_url,
        session,
        spec_id,
        u_name
    )
}