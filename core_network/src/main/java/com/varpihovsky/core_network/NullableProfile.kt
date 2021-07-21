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

data class NullableProfile(
    val course_num: Int?,
    val d_id: String?,
    val d_name: String?,
    val dob: String?,
    val email: String?,
    val f_id: String?,
    val gr_id: String?,
    val gr_name: String?,
    val id: String?,
    val photo_url: String?,
    val session: String?,
    val spec_id: String?,
    val u_name: String?
)
