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
package com.varpihovsky.ui_root.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core.di.get
import com.varpihovsky.core_lifecycle.LocalPaddingValues
import com.varpihovsky.core_ui.theme.JetIQTheme
import com.varpihovsky.ui_root.appbar.Appbar
import com.varpihovsky.ui_root.appbar.AppbarComponent
import com.varpihovsky.ui_root.bottomBar.BottomBar
import com.varpihovsky.ui_root.exceptions.ExceptionProcessor
import soup.compose.material.motion.*

@Composable
fun Root() {
    val rootComponent: RootComponent = get()

    JetIQTheme {
        Root(rootComponent = rootComponent)
    }
}

private fun Root(rootComponent: RootComponent) {
    val scaffoldState = rememberScaffoldState()
    val routerState by rootComponent.routerState.subscribeAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar(rootComponent.bottomBarComponent) }
    ) {
        ExceptionProcessor(rootComponent.exceptionComponent)

        RootLayout(
            paddingValues = it,
            appbarComponent = rootComponent.appbarComponent,
            state = routerState
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootLayout(
    paddingValues: PaddingValues,
    appbarComponent: AppbarComponent,
    state: RouterState<RootRouter.Config, RootChild>
) {
    SubcomposeLayout { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(constraints.maxWidth, constraints.maxHeight) {
            val appbar = subcompose(RootLayout.APP_BAR) {
                Appbar(appbarComponent = appbarComponent)
            }.map { it.measure(looseConstraints) }

            val appbarHeight = appbar.maxByOrNull { it.height }?.height ?: 0

            subcompose(RootLayout.NAV) {
                CompositionLocalProvider(
                    LocalPaddingValues provides PaddingValues(
                        top = appbarHeight.toDp(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
                    )
                ) {
                    Children(
                        routerState = state,
                        animation = { state, content ->
                            var children by rememberSaveable { mutableStateOf(0) }

                            val forward = children <= state.backStack.size

                            children = state.backStack.size

                            MaterialMotion(
                                targetState = state.activeChild,
                                motionSpec = when (state.activeChild.configuration) {
                                    RootRouter.Config.Auth -> materialSharedAxisY(forward, 200)
                                    RootRouter.Config.Profile -> materialSharedAxisX(forward, 200)
                                    RootRouter.Config.Messages -> materialSharedAxisX(forward, 200)
                                }
                            ) {
                                content(it)
                            }
                        }
                    ) {
                        when (it.instance) {
                            is RootChild.Auth -> TODO()
                            is RootChild.Profile -> TODO()
                            is RootChild.Messages -> TODO()
                        }
                    }
                }
            }.map { it.measure(looseConstraints) }.forEach { it.place(0, 0) }

            appbar.forEach { it.place(0, 0) }
        }
    }
}

private enum class RootLayout { APP_BAR, NAV }
