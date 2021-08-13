package com.varpihovsky.core_repo.apiMappers

import com.varpihovsky.jetiqApi.data.*
import com.varpihovsky.repo_data.*
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks

fun Message.toDTO() = MessageDTO(body, idFrom, isTeacher, id, time)

fun Profile.toDTO() = ProfileDTO(
    course,
    departmentId,
    departmentName,
    null,
    email,
    facultyId,
    groupId,
    groupName,
    id,
    photoUrl,
    session,
    specialityId,
    fullName
)

fun Subject.toDTO() = SubjectDTO(subjectId, controlForm, scale, semester, subject, teacherName)

fun SubjectDetails.toDTO() = SubjectDetailsWithTasks(
    SubjectDetailsDTO(
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
        totalHundredPointMarkPrevious
    ),
    tasks.map { it.toDTO(id) }
)

fun Task.toDTO(detailsId: Int) = SubjectTaskDTO(id, detailsId, legend, module, points)

fun MarkbookSubject.toDTO() = MarkbookSubjectDTO(id, credits, date, ects, form, hours, mark)