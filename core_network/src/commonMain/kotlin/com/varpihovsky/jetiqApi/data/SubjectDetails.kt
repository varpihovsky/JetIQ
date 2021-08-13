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
package com.varpihovsky.jetiqApi.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SubjectDetails(
    val id: Int,
    @SerialName("ects")
    val ectsMark: String,
    @SerialName("for_pres1")
    val forPresenceMarkFirstModule: Int,
    @SerialName("for_pres2")
    val forPresenceMarkSecondModule: Int,
    @SerialName("h_pres1")
    val hoursOfAbsenceFirstModule: Int,
    @SerialName("h_pres2")
    val hoursOfAbsenceSecondModule: Int,
    @SerialName("mark1")
    val fivePointMarkFirstModule: Int,
    @SerialName("mark2")
    val fivePointMarkSecondModule: Int,
    @SerialName("sum1")
    val hundredPointMarkFirstModule: Int,
    @SerialName("sum2")
    val hundredPointMarkSecondModule: Int,
    @SerialName("total")
    val totalHundredPointMark: Int,
    @SerialName("total_prev")
    val totalHundredPointMarkPrevious: Int,
    val tasks: List<Task>
)