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

expect class NullableProfile(
    course: Int?,
    departmentId: String?,
    departmentName: String?,
    email: String?,
    facultyId: String?,
    groupId: String?,
    groupName: String?,
    id: String?,
    photoUrl: String?,
    session: String?,
    specId: String?,
    fullName: String?
) {
    val course: Int?
    val departmentId: String?
    val departmentName: String?
    val email: String?
    val facultyId: String?
    val groupId: String?
    val groupName: String?
    val id: String?
    val photoUrl: String?
    val session: String?
    val specId: String?
    val fullName: String?
}
