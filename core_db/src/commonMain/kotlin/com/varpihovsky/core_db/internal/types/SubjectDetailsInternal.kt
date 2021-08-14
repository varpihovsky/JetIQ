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

import com.varpihovsky.core_db.internal.types.lists.SubjectDetailsList
import com.varpihovsky.repo_data.Listable
import kotlinx.serialization.Serializable
import org.kodein.db.Key

@Serializable
internal class SubjectDetailsInternal(
    override val id: Int,
    val ectsMark: String,
    val forPresenceMarkFirstModule: Int,
    val forPresenceMarkSecondModule: Int,
    val hoursOfAbsenceFirstModule: Int,
    val hoursOfAbsenceSecondModule: Int,
    val fivePointMarkFirstModule: Int,
    val fivePointMarkSecondModule: Int,
    val hundredPointMarkFirstModule: Int,
    val hundredPointMarkSecondModule: Int,
    val totalHundredPointMark: Int,
    val totalHundredPointMarkPrevious: Int,
    val tasks: List<Key<TaskInternal>>
) : Listable<SubjectDetailsList> {
    override fun with(id: Int): Listable<SubjectDetailsList> = SubjectDetailsInternal(
        id,
        ectsMark,
        forPresenceMarkFirstModule,
        forPresenceMarkSecondModule,
        hoursOfAbsenceFirstModule,
        hoursOfAbsenceSecondModule,
        fivePointMarkFirstModule,
        fivePointMarkSecondModule,
        hundredPointMarkFirstModule,
        hundredPointMarkSecondModule,
        totalHundredPointMark,
        totalHundredPointMarkPrevious,
        tasks
    )
}