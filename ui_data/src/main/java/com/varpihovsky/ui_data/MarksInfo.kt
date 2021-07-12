package com.varpihovsky.ui_data

data class MarksInfo(
    val semester: Int,
    val grade: Int
)

fun <T, S : Comparable<S>> formMarksInfo(
    array: Iterable<T>,
    semesterSelector: (T) -> Int,
    gradeSelector: (T) -> Int?,
    sortSelector: ((T) -> S?)? = null
): List<MarksInfo> {
    val marksInfo = mutableListOf<MarksInfo>()
    val resultArray = if (sortSelector != null) array.sortedBy { sortSelector(it) } else array

    var currentSemester = 1
    var grade = 0
    var subIndex = 0

    resultArray.forEach {
        if (semesterSelector(it) != currentSemester) {
            marksInfo.add(MarksInfo(currentSemester, grade / subIndex))
            grade = 0
            subIndex = 0
            currentSemester++
        }
        if (it == resultArray.last()) {
            subIndex++
            gradeSelector(it)?.let { selected -> grade += selected }
            marksInfo.add(MarksInfo(currentSemester, grade / subIndex))
        }
        gradeSelector(it)?.let { selected -> grade += selected }
        subIndex++
    }
    return marksInfo
}