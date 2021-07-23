package com.varpihovsky.core_network

import com.google.gson.annotations.SerializedName

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

data class NullableProfile(
    @SerializedName("course_num")
    val course: Int?,
    @SerializedName("d_id")
    val departmentId: String?,
    @SerializedName("d_name")
    val departmentName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("f_id")
    val facultyId: String?,
    @SerializedName("gr_id")
    val groupId: String?,
    @SerializedName("gr_name")
    val groupName: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("photo_url")
    val photoUrl: String?,
    @SerializedName("session")
    val session: String?,
    @SerializedName("spec_id")
    val specId: String?,
    @SerializedName("u_name")
    val fullName: String?
)
