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
package com.varpihovsky.core_db.internal.types.mappers

import com.varpihovsky.core_db.internal.types.*
import com.varpihovsky.jetiqApi.data.*
import com.varpihovsky.repo_data.MessageToSendDTO

internal fun Message.toInternal() = MessageInternal(body, idFrom, isTeacher, id.toInt(), time)

internal fun MessageInternal.toExternal() = Message(body, idFrom, isTeacher, id.toString(), time)

internal fun Profile.toInternal() = ProfileInternal(
    id.toInt(),
    fullName,
    course,
    departmentId.toInt(),
    departmentName,
    email,
    facultyId.toInt(),
    groupId.toInt(),
    groupName,
    photoUrl,
    session,
    specialityId.toInt()
)

internal fun ProfileInternal.toExternal() = Profile(
    id.toString(),
    fullName,
    course,
    departmentId.toString(),
    departmentName,
    email,
    facultyId.toString(),
    groupId.toString(),
    groupName,
    photoUrl,
    session,
    specialityId.toString()
)

internal fun SubjectInternal.toExternal() = Subject(
    id.toString(),
    controlForm,
    scale,
    semester.toString(),
    subject,
    teacherName
)

internal fun Subject.toInternal() = SubjectInternal(
    subjectId.toInt(),
    controlForm,
    scale,
    semester.toInt(),
    subject,
    teacherName
)

internal fun SubjectDetails.toInternal() = SubjectDetailsInternal(
    id,
    ectsMark,
    forPresenceMarkFirstModule,
    forPresenceMarkSecondModule,
    hoursOfAbsenceFirstModule,
    hoursOfAbsenceSecondModule,
    fivePointMarkFirstModule,
    fivePointMarkSecondModule,
    hundredPointMarkFirstModule,
    hundredPointMarkSecondModule,
    totalHundredPointMark,
    totalHundredPointMarkPrevious,
    tasks
)

internal fun SubjectDetailsInternal.toExternal() = SubjectDetails(
    id,
    ectsMark,
    forPresenceMarkFirstModule,
    forPresenceMarkSecondModule,
    hoursOfAbsenceFirstModule,
    hoursOfAbsenceSecondModule,
    fivePointMarkFirstModule,
    fivePointMarkSecondModule,
    hundredPointMarkFirstModule,
    hundredPointMarkSecondModule,
    totalHundredPointMark,
    totalHundredPointMarkPrevious,
    tasks
)

internal fun MarkbookSubject.toInternal() = MarkbookSubjectInternal(
    id,
    credits,
    date,
    ects,
    form,
    hours,
    mark,
    subjectName,
    teacher,
    total,
    semester
)

internal fun MarkbookSubjectInternal.toExternal() = MarkbookSubject(
    id,
    credits,
    date,
    ects,
    form,
    hours,
    mark,
    subjectName,
    teacher,
    total,
    semester
)

internal fun MessageToSendDTO.toInternal(id: Int) = SentMessageInternal(id, this.id, type, body)

internal fun SentMessageInternal.toExternal() = MessageToSendDTO(receiverId, type, body)