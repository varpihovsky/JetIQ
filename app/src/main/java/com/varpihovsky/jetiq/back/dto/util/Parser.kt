package com.varpihovsky.jetiq.back.dto.util

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.varpihovsky.jetiq.back.dto.MarkbookSubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDTO
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.SubjectTaskDTO

fun deserializeMarkbookSubjects(json: String): List<MarkbookSubjectDTO> {
    val gson = Gson()
    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject
    val members = mutableListOf<MarkbookSubjectDTO>()

    for ((key, value) in jsonObj.entrySet()) {
        if (isNumeric(key)) {
            val array = value.asJsonObject
            for ((k, v) in array.entrySet()) {
                members.add(
                    gson.fromJson(v, MarkbookSubjectDTO::class.java).withSemester(key.toInt())
                )
            }
        }
    }
    return members
}

fun deserializeSubjectDetails(json: String): SubjectDetailsDTO {
    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject

    return SubjectDetailsDTO(
        0,
        jsonObj.get("ects").asString,
        jsonObj.get("for_pres1").asInt,
        jsonObj.get("for_pres2").asInt,
        jsonObj.get("h_pres1").asInt,
        jsonObj.get("h_pres2").asInt,
        jsonObj.get("mark1").asInt,
        jsonObj.get("mark2").asInt,
        jsonObj.get("sum1").asInt,
        jsonObj.get("sum2").asInt,
        jsonObj.get("total").asInt,
        jsonObj.get("total_prev").asInt
    )
}

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

