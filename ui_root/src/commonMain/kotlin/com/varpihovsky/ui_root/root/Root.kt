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
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.composition.CompositionState
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.feature_auth.Auth
import com.varpihovsky.feature_messages.MessagesScreen
import com.varpihovsky.feature_profile.ProfileScreen
import com.varpihovsky.feature_settings.SettingsScreen
import com.varpihovsky.ui_root.appbar.Appbar
import com.varpihovsky.ui_root.appbar.AppbarComponent
import com.varpihovsky.ui_root.bottomBar.BottomBar
import com.varpihovsky.ui_root.drawer.Drawer
import com.varpihovsky.ui_root.drawer.DrawerComponent
import com.varpihovsky.ui_root.exceptions.ExceptionProcessor
import soup.compose.material.motion.MaterialMotion
import soup.compose.material.motion.materialSharedAxisX
import soup.compose.material.motion.materialSharedAxisY

private const val DRAWER_BUTTON_SHOWING_THRESHOLD = 800
private const val DEFAULT_DRAG_VELOCITY = 100

@Composable
fun Root(rootComponent: RootComponent) {
    val scaffoldState = rememberScaffoldState()
    val routerState = rootComponent.routerState

    BoxWithConstraints {
        val isDrawerButtonShown = maxWidth >= DRAWER_BUTTON_SHOWING_THRESHOLD.dp

        LaunchedEffect(isDrawerButtonShown) {
            val component = rootComponent.bottomBarComponent
            if (isDrawerButtonShown) {
                component.hide()
            } else {
                component.show()
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = { BottomBar(rootComponent.bottomBarComponent) },
        ) {
            ExceptionProcessor(rootComponent.exceptionComponent)

            RootLayout(
                paddingValues = it,
                appbarComponent = rootComponent.appbarComponent,
                routerState = routerState,
                drawerState = rootComponent.drawerComponent,
                isDrawerButtonShown = isDrawerButtonShown
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootLayout(
    paddingValues: PaddingValues,
    appbarComponent: AppbarComponent,
    routerState: Value<RouterState<RootRouter.Config, RootComponent.RootChild>>,
    drawerState: DrawerComponent,
    isDrawerButtonShown: Boolean
) {
    SubcomposeLayout { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(constraints.maxWidth, constraints.maxHeight) {
            val appbar = subcompose(RootLayout.APP_BAR) {
                Appbar(
                    appbarComponent = appbarComponent,
                    drawerState = drawerState,
                    isDrawerButtonShown = isDrawerButtonShown
                )
            }.map { it.measure(looseConstraints) }

            val appbarHeight = appbar.maxByOrNull { it.height }?.height ?: 0

            subcompose(RootLayout.NAV) {
                LocalCompositionState provides CompositionState(
                    paddingValues = PaddingValues(
                        top = appbarHeight.toDp(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
                    ), currentMode = if (isDrawerButtonShown) Mode.Desktop else Mode.Mobile
                )
                Row {
                    val isShown by drawerState.isShown.subscribeAsState()

                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.width(250.dp).padding(top = appbarHeight.toDp()),
                        visible = isShown && isDrawerButtonShown,
                        enter = expandHorizontally(expandFrom = Alignment.End),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.End)
                    ) {
                        Drawer(drawerState, isDrawerButtonShown)
                    }
                    Box {
                        Children(
                            routerState = routerState,
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
                                        RootRouter.Config.Settings -> materialSharedAxisX(forward, 200)
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.draggable(
                                            state = rememberDraggableState { },
                                            orientation = Orientation.Horizontal,
                                            onDragStopped = { velocity ->
                                                if (velocity > DEFAULT_DRAG_VELOCITY && !drawerState.isShown.value) {
                                                    drawerState.toggle()
                                                } else if (velocity < -DEFAULT_DRAG_VELOCITY && drawerState.isShown.value) {
                                                    drawerState.toggle()
                                                }
                                            }),
                                    ) { content(it) }
                                }
                            }
                        ) {
                            when (val child = it.instance) {
                                is RootComponent.RootChild.Auth ->
                                    Box(modifier = Modifier.padding(LocalCompositionState.current.paddingValues)) {
                                        Auth(child.component)
                                    }
                                is RootComponent.RootChild.Profile -> ProfileScreen(child.component)
                                is RootComponent.RootChild.Messages ->
                                    Box(modifier = Modifier.padding(LocalCompositionState.current.paddingValues)) {
                                        MessagesScreen(child.component)
                                    }
                                is RootComponent.RootChild.Settings ->
                                    Box(modifier = Modifier.padding(LocalCompositionState.current.paddingValues)) {
                                        SettingsScreen(child.component)
                                    }
                            }
                        }
                    }
                }
            }.map { it.measure(looseConstraints) }.forEach { it.place(0, 0) }

            appbar.forEach { it.place(0, 0) }
        }
    }
}

private enum class RootLayout { APP_BAR, NAV }
