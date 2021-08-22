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
package com.varpihovsky.ui_root.bottomBar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core_lifecycle.BottomBarEntry

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(bottomBarComponent: BottomBarComponent) {
    val isShown = bottomBarComponent.isShown.subscribeAsState()

    AnimatedVisibility(
        visible = isShown.value,
        enter = slideInVertically(),
        exit = shrinkHorizontally()
    ) {
        BottomAppBar {
            val currentEntry by bottomBarComponent.entry.subscribeAsState()

            BottomNavigationMenuButton(
                checked = currentEntry == BottomBarEntry.Messages,
                onClick = { bottomBarComponent.select(BottomBarEntry.Messages) },
                painter = rememberVectorPainter(Icons.Default.Message),
                contentDescription = "Повідомлення",
                buttonSubscription = "Повідомлення"
            )
            BottomNavigationMenuButton(
                checked = currentEntry == BottomBarEntry.Profile,
                onClick = { bottomBarComponent.select(BottomBarEntry.Profile) },
                painter = rememberVectorPainter(Icons.Default.Person),
                contentDescription = "Профіль",
                buttonSubscription = "Профіль"
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BottomNavigationMenuButton(
    checked: Boolean,
    painter: Painter,
    contentDescription: String?,
    buttonSubscription: String,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(targetValue = if (checked) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground)

    IconButton(
        modifier = Modifier.wrapContentSize(unbounded = true),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.wrapContentSize(unbounded = true),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(painter = painter, contentDescription = contentDescription, tint = iconColor)
            AnimatedVisibility(visible = checked) {
                Text(
                    text = buttonSubscription,
                    style = MaterialTheme.typography.caption.copy(color = iconColor)
                )
            }
        }
    }
}
