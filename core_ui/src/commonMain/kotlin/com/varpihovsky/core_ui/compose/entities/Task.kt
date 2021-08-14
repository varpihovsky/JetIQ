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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.widgets.ExpandableList
import com.varpihovsky.jetiqApi.data.Task

@ExperimentalAnimationApi
@Composable
fun TaskList(
    title: String,
    tasks: List<Task>,
    sum: String,
    mark: String
) {
    ExpandableList(
        modifier = Modifier.background(color = MaterialTheme.colors.surface),
        title = title
    ) {
        Column {
            tasks.forEach {
                Task(
                    modifier = Modifier.padding(vertical = 7.dp),
                    legend = it.legend,
                    points = it.points.toString()
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
            Task(
                modifier = Modifier.padding(vertical = 7.dp),
                legend = "Сума балів",
                points = sum
            )
            Divider(modifier = Modifier.fillMaxWidth())
            Task(
                modifier = Modifier.padding(vertical = 7.dp),
                legend = "Оцінка в 5-бальній системі",
                points = mark
            )
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun Task(modifier: Modifier = Modifier, legend: String, points: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp, end = 5.dp)
                .weight(3f),
            text = legend,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
        )
        Text(
            modifier = Modifier
                .padding(start = 5.dp, end = 10.dp)
                .weight(1f, true),
            text = points,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}
