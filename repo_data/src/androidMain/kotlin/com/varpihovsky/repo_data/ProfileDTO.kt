package com.varpihovsky.repo_data

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

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
actual data class ProfileDTO(
    actual val course_num: Int,
    actual val d_id: String,
    actual val d_name: String,
    actual val dob: String?,
    actual val email: String,
    actual val f_id: String,
    actual val gr_id: String,
    actual val gr_name: String,
    @PrimaryKey(autoGenerate = false) actual val id: String,
    actual val photo_url: String,
    actual val session: String?,
    actual val spec_id: String,
    actual val u_name: String
)