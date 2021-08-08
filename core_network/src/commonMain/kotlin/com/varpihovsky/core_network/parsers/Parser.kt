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

import com.varpihovsky.repo_data.*

expect fun deserializeMarkbookSubjects(json: String): List<MarkbookSubjectDTO>

expect fun deserializeSubjectDetails(json: String): SubjectDetailsDTO

expect fun deserializeSubjects(json: String): List<SubjectDTO>

expect fun deserializeSubjectTasks(json: String): List<SubjectTaskDTO>

fun deserializeTeachers(json: String): List<ListItemDTO> =
    try {
        deserializeListItem(json, ListItemJsonKey.TEACHER)
    } catch (e: Exception) {
        listOf()
    }

expect fun deserializeListItem(json: String?, primaryKey: ListItemJsonKey): List<ListItemDTO>

enum class ListItemJsonKey {
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


fun isNumeric(string: String) = string.matches("-?\\d+(\\.\\d+)?".toRegex())

