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
class Subject(
    @SerialName("card_id")
    val subjectId: String,
    @SerialName("f_control")
    val controlForm: String,
    val scale: String,
    @SerialName("sem")
    val semester: String,
    val subject: String,
    @SerialName("t_name")
    val teacherName: String
)