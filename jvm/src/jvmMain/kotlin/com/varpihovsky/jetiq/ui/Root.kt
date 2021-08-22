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
package com.varpihovsky.jetiq.ui

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.di.getViewModel
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_nav.dsl.DisplayNavigation
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.JetNav
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_ui.compose.ExceptionProcessor
import com.varpihovsky.core_ui.compose.widgets.Appbar
import com.varpihovsky.feature_auth.Auth
import com.varpihovsky.feature_messages.contacts.ContactsScreen
import com.varpihovsky.feature_messages.wall.MessageWall
import com.varpihovsky.feature_new_message.NewMessageScreen
import com.varpihovsky.feature_profile.profile.Profile
import com.varpihovsky.feature_profile.subjects.markbook.MarkbookSubjectScreen
import com.varpihovsky.feature_profile.subjects.success.SuccessSubjectScreen
import com.varpihovsky.feature_settings.about.AboutSettingsScreen
import com.varpihovsky.feature_settings.general.GeneralSettingsScreen
import com.varpihovsky.feature_settings.main.MainSettingsScreen
import com.varpihovsky.jetiq.NavigationViewModel
import soup.compose.material.motion.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Root(
    navigationViewModel: NavigationViewModel,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
    eventBus: EventBus
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        ExceptionProcessor(exceptionEventManager = exceptionEventManager)

        val navigationController = initNavigation(
            navigationViewModel = navigationViewModel,
            eventBus = eventBus
        )

        RootLayout(
            appbarManager = appbarManager,
            paddingValues = paddingValues,
            navigationController = navigationController
        )

    }
}


@ExperimentalAnimationApi
@Composable
fun RootLayout(
    appbarManager: AppbarManager,
    paddingValues: PaddingValues,
    navigationController: NavigationController
) {
    SubcomposeLayout { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(constraints.maxWidth, constraints.maxHeight) {
            val appbar = subcompose(RootLayout.APP_BAR) {
                Appbar(appbarManager = appbarManager)
            }.map { it.measure(looseConstraints) }

            val appbarHeight = appbar.maxByOrNull { it.height }?.height ?: 0

            subcompose(RootLayout.NAV) {
                DisplayNavigation(
                    modifier = Modifier.padding(paddingValues = paddingValues),
                    controller = navigationController,
                    paddingValues = PaddingValues(
                        top = appbarHeight.toDp()
                    )
                )
            }.map { it.measure(looseConstraints) }.forEach { it.place(0, 0) }

            appbar.forEach { it.place(0, 0) }
        }
    }
}

private enum class RootLayout { APP_BAR, NAV }

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
@ExperimentalFoundationApi
fun initNavigation(
    navigationViewModel: NavigationViewModel,
    eventBus: EventBus
): NavigationController {
    val startDestination = remember { navigationViewModel.getStartDestination() }

    JetNav.createControllerIfNotCreated(eventBus, startDestination) {
        entry {
            val entryRoute = NavigationDirections.authentication.destination
            composable = { Auth(viewModel = getViewModel()) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.newMessage.destination
            composable =
                {

                    NewMessageScreen(
                        newMessageComponent = getViewModel()
                    )
                }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.contacts.destination
            composable =
                {

                    ContactsScreen(
                        contactsComponent = getViewModel()
                    )
                }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThroughIn()
            outAnimation = materialFadeThroughOut()
        }
        entry {
            val entryRoute = NavigationDirections.mainSettings.destination
            composable = {

                MainSettingsScreen(mainSettingsComponent = getViewModel())
            }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThroughIn()
            outAnimation = materialFadeThroughOut()
        }
        entry {
            val entryRoute = NavigationDirections.aboutSettings.destination
            composable = {

                AboutSettingsScreen(aboutSettingsViewModel = getViewModel())
            }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.generalSettings.destination
            composable = {

                GeneralSettingsScreen(generalSettingsComponent = getViewModel())
            }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.markbookSubject.destination
            composable = {

                MarkbookSubjectScreen(markbookSubjectComponent = getViewModel())
            }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.successSubject.destination
            composable = {

                SuccessSubjectScreen(successSubjectComponent = getViewModel())
            }

            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.messages.destination
            composable =
                { MessageWall(messagesComponent = getViewModel()) }
            route = entryRoute
            entryType = EntryType.Main
            inAnimation = materialSharedAxisXIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisXOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.profile.destination
            applyPaddingValues = false
            composable = { values ->
                Profile(
                    paddingValues = values
                )
            }

            route = entryRoute
            entryType = EntryType.Main
            inAnimation = materialSharedAxisXIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisXOut(forward = false, slideDistance = 200)
        }
    }

    return JetNav.getController()
}

