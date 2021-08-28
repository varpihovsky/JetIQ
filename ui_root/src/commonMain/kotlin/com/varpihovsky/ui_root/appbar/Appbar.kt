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
package com.varpihovsky.ui_root.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.ui_root.drawer.DrawerComponent
import soup.compose.material.motion.MaterialSharedAxisY

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun Appbar(appbarComponent: AppbarComponent, drawerState: DrawerComponent, isDrawerButtonShown: Boolean) {
    val isShown by appbarComponent.isShown.subscribeAsState()
    val text by appbarComponent.text.subscribeAsState()
    val actions by appbarComponent.actions.subscribeAsState()
    val navigationIcon by appbarComponent.navigationIcon.subscribeAsState()

    AnimatedVisibility(
        visible = isShown,
        enter = slideInVertically(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top)
    ) {
        TopAppBar(
            title = {
                MaterialSharedAxisY(
                    targetState = text,
                    forward = true
                ) {
                    Text(text = it)
                }
            },
            navigationIcon = when (navigationIcon) {
                NavigationIconState.Empty -> null
                NavigationIconState.Back -> @Composable {
                    {
                        IconButton(
                            onClick = { appbarComponent.backPressedDispatcher.onBackPressed() }
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                }
                NavigationIconState.Drawer -> @Composable {
                    {
                        if (isDrawerButtonShown) {
                            IconButton(onClick = { drawerState.toggle() }) {
                                Icon(imageVector = Icons.Default.Dashboard, contentDescription = null)
                            }
                        }

                    }
                }
            },
            actions = actions
        )
    }
}