package com.varpihovsky.core_network.parsers

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.varpihovsky.repo_data.*

actual fun deserializeMarkbookSubjects(json: String): List<MarkbookSubjectDTO> {
    val gson = Gson()
    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject
    val members = mutableListOf<MarkbookSubjectDTO>()

    for ((key, value) in jsonObj.entrySet()) {
        if (isNumeric(key)) {
            val array = value.asJsonObject
            for ((_, v) in array.entrySet()) {
                members.add(
                    gson.fromJson(v, MarkbookSubjectDTO::class.java).copy(semester = key.toInt())
                )
            }
        }
    }
    return members
}

actual fun deserializeSubjectDetails(json: String): SubjectDetailsDTO {
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

actual fun deserializeSubjects(json: String): List<SubjectDTO> {
    if (json.contains("st_group is empty")) {
        return emptyList()
    }

    val gson = Gson()
    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject
    val members = mutableListOf<SubjectDTO>()

    for ((k, value) in jsonObj.entrySet()) {
        members.add(gson.fromJson(value, SubjectDTO::class.java))
    }
    return members
}

actual fun deserializeSubjectTasks(json: String): List<SubjectTaskDTO> {
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

actual fun deserializeListItem(json: String?, primaryKey: ListItemJsonKey): List<ListItemDTO> {
    if (json == " null") {
        return emptyList()
    }

    val parser = JsonParser()
    val jsonObj = parser.parse(json).asJsonObject
    val members = mutableListOf<ListItemDTO>()

    val facultiesObj = jsonObj.get(primaryKey.get()).asJsonObject

    for ((key, value) in facultiesObj.entrySet()) {
        members.add(ListItemDTO(key.toInt(), value.asString))
    }
    return members
}