package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO

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

    fun toUIDTO() = UIProfileDTO(
        id.toInt(),
        getUsername(),
        cutFacultyName(),
        course_num,
        gr_name,
        0,
        photo_url
    )

    private fun getUsername(): String {
        val strings = u_name.split(" ").subList(0, 2)
        return "${strings[0]} ${strings[1]}"
    }

    private fun cutFacultyName() =
        String(d_name.split(" ").filter { it.length > 2 }.map { it.first().uppercaseChar() }
            .toCharArray())
}