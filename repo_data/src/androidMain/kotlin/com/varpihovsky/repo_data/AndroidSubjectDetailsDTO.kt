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
actual data class SubjectDetailsDTO actual constructor(
    @PrimaryKey(autoGenerate = false) actual val id: Int,
    actual val ects: String,
    actual val for_pres1: Int,
    actual val for_pres2: Int,
    actual val h_pres1: Int,
    actual val h_pres2: Int,
    actual val mark1: Int,
    actual val mark2: Int,
    actual val sum1: Int,
    actual val sum2: Int,
    actual val total: Int,
    actual val total_prev: Int
) {
    actual fun withID(id: Int) = SubjectDetailsDTO(
        id,
        ects,
        for_pres1,
        for_pres2,
        h_pres1,
        h_pres2,
        mark1,
        mark2,
        sum1,
        sum2,
        total,
        total_prev
    )
}
