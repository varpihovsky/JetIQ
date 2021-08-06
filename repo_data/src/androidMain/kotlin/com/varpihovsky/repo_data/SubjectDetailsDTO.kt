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
actual data class SubjectDetailsDTO(
    @PrimaryKey(autoGenerate = false) actual val id: Int = 0,
    actual val ects: String = "",
    actual val for_pres1: Int = 0,
    actual val for_pres2: Int = 0,
    actual val h_pres1: Int = 0,
    actual val h_pres2: Int = 0,
    actual val mark1: Int = 0,
    actual val mark2: Int = 0,
    actual val sum1: Int = 0,
    actual val sum2: Int = 0,
    actual val total: Int = 0,
    actual val total_prev: Int = 0
)

