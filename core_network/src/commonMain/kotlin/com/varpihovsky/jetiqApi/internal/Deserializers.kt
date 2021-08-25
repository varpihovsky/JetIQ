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
package com.varpihovsky.jetiqApi.internal

import com.varpihovsky.jetiqApi.data.*
import kotlinx.serialization.json.*
import kotlin.math.roundToInt

internal fun deserializeMarkbookSubjects(json: String): List<MarkbookSubject> {
    val jsonObj = Json.parseToJsonElement(json).jsonObject
    val members = mutableListOf<MarkbookSubject>()

    for ((key, value) in jsonObj.entries) {
        if (key.isNumeric()) {
            val array = value.jsonObject
            for ((_, v) in array.entries) {
                members.add(
                    Json.decodeFromJsonElement<MarkbookSubject>(v).withSemester(key.toInt())
                )
            }
        }
    }

    return members
}

internal fun deserializeSubjectDetails(json: String): SubjectDetails {
    val jsonObj = Json.parseToJsonElement(json).jsonObject

    return SubjectDetails(
        0,
        jsonObj["ects"]!!.jsonPrimitive.content,
        jsonObj["for_pres1"]!!.jsonPrimitive.int,
        jsonObj["for_pres2"]!!.jsonPrimitive.int,
        jsonObj["h_pres1"]!!.jsonPrimitive.int,
        jsonObj["h_pres2"]!!.jsonPrimitive.int,
        jsonObj["mark1"]!!.jsonPrimitive.int,
        jsonObj["mark2"]!!.jsonPrimitive.int,
        jsonObj["sum1"]!!.jsonPrimitive.float.roundToInt(),
        jsonObj["sum2"]!!.jsonPrimitive.float.roundToInt(),
        jsonObj["total"]!!.jsonPrimitive.float.roundToInt(),
        jsonObj["total_prev"]!!.jsonPrimitive.float.roundToInt(),
        deserializeSubjectTasks(json)
    )
}

internal fun deserializeSubjects(json: String): List<Subject> {
    if (json.contains("st_group is empty")) {
        return emptyList()
    }

    val jsonObj = Json.parseToJsonElement(json).jsonObject
    val members = mutableListOf<Subject>()

    for ((_, value) in jsonObj.entries) {
        members.add(Json.decodeFromJsonElement(value))
    }
    return members
}

internal fun deserializeSubjectTasks(json: String): List<Task> {
    val jsonObj = Json.parseToJsonElement(json).jsonObject
    val members = mutableListOf<Task>()

    for ((key, value) in jsonObj.entries) {
        if (key.isNumeric()) {
            members.add(Json.decodeFromJsonElement(value))
        }
    }
    return members
}

internal fun deserializeTeachers(json: String): List<Teacher> =
    try {
        deserializeListItem(json, ListItemJsonKey.TEACHER) { id, fullName -> Teacher(id, fullName) }
    } catch (e: Exception) {
        listOf()
    }

internal fun deserializeGroups(json: String): List<Group> =
    try {
        deserializeListItem(json, ListItemJsonKey.GROUP) { id, groupName -> Group(id, groupName) }
    } catch (e: Exception) {
        listOf()
    }

internal fun deserializeFaculties(json: String): List<Faculty> =
    try {
        deserializeListItem(json, ListItemJsonKey.FACULTY) { id, facultyName ->
            Faculty(
                id,
                facultyName
            )
        }
    } catch (e: Exception) {
        listOf()
    }

internal fun deserializeStudents(json: String): List<Student> =
    try {
        deserializeListItem(json, ListItemJsonKey.STUDENT) { id, fullName -> Student(id, fullName) }
    } catch (e: Exception) {
        listOf()
    }

private fun <T> deserializeListItem(
    json: String,
    primaryKey: ListItemJsonKey,
    factory: (Int, String) -> T
): List<T> {
    if (json == " null") {
        return emptyList()
    }

    val jsonObj = Json.parseToJsonElement(json).jsonObject
    val members = mutableListOf<T>()

    val facultiesObj = jsonObj[primaryKey.get()]!!.jsonObject

    for ((key, value) in facultiesObj.entries) {
        members.add(factory(key.toInt(), value.jsonPrimitive.content))
    }
    return members
}

private enum class ListItemJsonKey {
    GROUP {
        override fun get() = "st_group"
    },
    FACULTY {
        override fun get() = "d_name"
    },
    STUDENT {
        override fun get() = "s_name"
    },
    TEACHER {
        override fun get() = "t_name"
    };


    abstract fun get(): String
}


private fun String.isNumeric() = matches("-?\\d+(\\.\\d+)?".toRegex())
