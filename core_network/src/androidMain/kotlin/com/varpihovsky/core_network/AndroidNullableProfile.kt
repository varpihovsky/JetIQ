package com.varpihovsky.core_network

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

import com.google.gson.annotations.SerializedName

actual data class NullableProfile actual constructor(
    @SerializedName("course_num")
    actual val course: Int?,
    @SerializedName("d_id")
    actual val departmentId: String?,
    @SerializedName("d_name")
    actual val departmentName: String?,
    @SerializedName("email")
    actual val email: String?,
    @SerializedName("f_id")
    actual val facultyId: String?,
    @SerializedName("gr_id")
    actual val groupId: String?,
    @SerializedName("gr_name")
    actual val groupName: String?,
    @SerializedName("id")
    actual val id: String?,
    @SerializedName("photo_url")
    actual val photoUrl: String?,
    @SerializedName("session")
    actual val session: String?,
    @SerializedName("spec_id")
    actual val specId: String?,
    @SerializedName("u_name")
    actual val fullName: String?
)