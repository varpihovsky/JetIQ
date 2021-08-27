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
package com.varpihovsky.core_ui.compose.foundation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import kotlin.math.ceil

@Composable
fun CenterLayout(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        content()
    }
}

@Composable
fun CenterLayoutItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}

@Composable
fun VerticalScrollLayout(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        content()
    }
}

@Composable
fun SwipeRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    indicatorPadding: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {
    SwipeRefreshActual(modifier, isRefreshing, onRefresh, indicatorPadding, content)
}

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}

@Composable
internal expect fun SwipeRefreshActual(
    modifier: Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    indicatorPadding: PaddingValues,
    content: @Composable () -> Unit
)