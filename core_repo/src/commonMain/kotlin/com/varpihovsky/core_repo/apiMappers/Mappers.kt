package com.varpihovsky.core_repo.apiMappers

import com.varpihovsky.jetiqApi.data.MarkbookSubject
import com.varpihovsky.jetiqApi.data.SubjectDetails
import com.varpihovsky.jetiqApi.data.Task

fun SubjectDetails.withID(id: Int) = SubjectDetails(
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

fun SubjectDetails.withTasks(tasks: List<Task>) = SubjectDetails(
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

fun Task.withID(id: Int) = Task(id, legend, module, points)

fun MarkbookSubject.withID(id: Int) = MarkbookSubject(
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