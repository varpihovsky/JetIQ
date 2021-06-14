package com.varpihovsky.jetiq.back.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonParser

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

fun deserializeSubjectTasks(json: String): List<SubjectTaskDTO> {
    val gson = Gson()
    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject
    val members = mutableListOf<SubjectTaskDTO>()

    for ((key, value) in jsonObj.entrySet()) {
        if (isNumeric(key)) {
            members.add(gson.fromJson(value, SubjectTaskDTO::class.java))
        }
    }
    return members
}

fun isNumeric(string: String) = string.matches("-?\\d+(\\.\\d+)?".toRegex())

