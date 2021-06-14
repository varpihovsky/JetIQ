package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonParser

@Entity
data class SubjectDTO(
    @PrimaryKey(autoGenerate = false) val card_id: String,
    val f_control: String,
    val scale: String,
    val sem: String,
    val subject: String,
    val t_name: String
)

fun deserializeSubjects(json: String): List<SubjectDTO> {
    val gson = Gson()
    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject
    val members = mutableListOf<SubjectDTO>()

    for ((_, value) in jsonObj.entrySet()) {
        members.add(gson.fromJson(value, SubjectDTO::class.java))
    }
    return members
}