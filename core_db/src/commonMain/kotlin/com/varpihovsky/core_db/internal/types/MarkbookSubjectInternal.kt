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
package com.varpihovsky.core_db.internal.types

import com.varpihovsky.repo_data.Listable
import kotlinx.serialization.Serializable

@Serializable
internal class MarkbookSubjectInternal(
    override val id: Int,
    val credits: String,
    val date: String,
    val ects: String,
    val form: String,
    val hours: String,
    val mark: String,
    val subjectName: String,
    val teacher: String,
    val total: Float,
    val semester: Int
) : Listable {
    override fun with(id: Int): Listable = MarkbookSubjectInternal(
        id,
        credits,
        date,
        ects,
        form,
        hours,
        mark,
        subjectName,
        teacher,
        total,
        semester
    )
}