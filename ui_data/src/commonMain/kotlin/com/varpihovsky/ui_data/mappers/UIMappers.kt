package com.varpihovsky.ui_data.mappers

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

import com.varpihovsky.jetiqApi.data.MarkbookSubject
import com.varpihovsky.jetiqApi.data.Message
import com.varpihovsky.jetiqApi.data.Profile
import com.varpihovsky.jetiqApi.data.Subject
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.ui_data.dto.*

fun ContactDTO.toUIDTO() = UIReceiverDTO(
    id,
    text,
    when (type) {
        ContactDTO.TYPE_STUDENT -> ReceiverType.STUDENT
        ContactDTO.TYPE_TEACHER -> ReceiverType.TEACHER
        else -> throw RuntimeException()
    }
)

fun MarkbookSubject.toUIDTO() = UISubjectDTO(
    id,
    subjectName,
    teacher,
    total,
    semester
)

expect fun Message.toUIDTO(): UIMessageDTO

fun Profile.toUIDTO() = UIProfileDTO(
    id.toInt(),
    getUsername(fullName),
    cutFacultyName(departmentName),
    course,
    groupName,
    0,
    photoUrl
)

private fun getUsername(name: String): String {
    val strings = name.split(" ").subList(0, 2)
    return "${strings[0]} ${strings[1]}"
}

private fun cutFacultyName(faculty: String) =
    faculty.split(" ").filter { it.length > 2 }.map { it.first().uppercaseChar() }
        .toCharArray().concatToString()

fun Subject.toUIDTO(total: Int) = UISubjectDTO(
    subjectId.toInt(),
    subject,
    teacherName,
    total,
    semester.toInt()
)