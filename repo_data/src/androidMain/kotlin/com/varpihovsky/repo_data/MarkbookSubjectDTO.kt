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
actual data class MarkbookSubjectDTO actual constructor(
    @PrimaryKey(autoGenerate = true) actual val id: Int,
    actual val credits: String,
    actual val date: String,
    actual val ects: String,
    actual val form: String,
    actual val hours: String,
    actual val mark: String,
    actual val subj_name: String,
    actual val teacher: String,
    actual val total: Int,
    actual val semester: Int
) {
    actual fun withID(id: Int) = MarkbookSubjectDTO(
        id,
        credits,
        date,
        ects,
        form,
        hours,
        mark,
        subj_name,
        teacher,
        total,
        semester
    )
}

