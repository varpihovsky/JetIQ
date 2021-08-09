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
package com.varpihovsky.core_ui.compose.entities

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.varpihovsky.core_ui.compose.widgets.BoxInfo
import com.varpihovsky.ui_data.dto.MarksInfo

@ExperimentalAnimationApi
@Composable
fun MarksList(
    marks: List<MarksInfo>
) {
    Column {
        for (mark in marks) {
            BoxInfo(
                bigText = mark.grade.toString(),
                smallText = "${mark.semester} Семестр"
            )
        }
    }
}