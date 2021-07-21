package com.varpihovsky.ui_data

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