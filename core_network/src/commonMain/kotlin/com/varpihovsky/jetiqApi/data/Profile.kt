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
class Profile(
    val id: String,
    @SerialName("u_name")
    val fullName: String,
    @SerialName("course_num")
    val course: Int,
    @SerialName("d_id")
    val departmentId: String,
    @SerialName("d_name")
    val departmentName: String,
    val email: String,
    @SerialName("f_id")
    val facultyId: String,
    @SerialName("gr_id")
    val groupId: String,
    @SerialName("gr_name")
    val groupName: String,
    @SerialName("photo_url")
    val photoUrl: String,
    val session: String?,
    @SerialName("spec_id")
    val specialityId: String,
) {
    fun withSession(session: String?) = Profile(
        id,
        fullName,
        course,
        departmentId,
        departmentName,
        email,
        facultyId,
        groupId,
        groupName,
        photoUrl,
        session,
        specialityId
    )
}