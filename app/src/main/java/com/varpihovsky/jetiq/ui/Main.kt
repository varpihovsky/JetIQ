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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.di.get
import com.varpihovsky.core.di.getViewModel
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_nav.dsl.DisplayNavigation
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.JetNav
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.BottomNavigationItem
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_ui.compose.ExceptionProcessor
import com.varpihovsky.core_ui.compose.widgets.Appbar
import com.varpihovsky.core_ui.compose.widgets.InfoCard
import com.varpihovsky.feature_auth.Auth
import com.varpihovsky.feature_auth.AuthComponent
import com.varpihovsky.feature_messages.contacts.ContactsComponent
import com.varpihovsky.feature_messages.contacts.ContactsScreen
import com.varpihovsky.feature_messages.wall.MessageWall
import com.varpihovsky.feature_messages.wall.MessagesComponent
import com.varpihovsky.feature_new_message.NewMessageComponent
import com.varpihovsky.feature_new_message.NewMessageScreen
import com.varpihovsky.feature_profile.profile.Profile
import com.varpihovsky.feature_profile.profile.ProfileComponent
import com.varpihovsky.feature_profile.subjects.markbook.MarkbookSubjectComponent
import com.varpihovsky.feature_profile.subjects.markbook.MarkbookSubjectScreen
import com.varpihovsky.feature_profile.subjects.success.SuccessSubjectComponent
import com.varpihovsky.feature_profile.subjects.success.SuccessSubjectScreen
import com.varpihovsky.feature_settings.about.AboutSettingsComponent
import com.varpihovsky.feature_settings.about.AboutSettingsScreen
import com.varpihovsky.feature_settings.general.GeneralSettingsComponent
import com.varpihovsky.feature_settings.general.GeneralSettingsScreen
import com.varpihovsky.feature_settings.main.MainSettingsComponent
import com.varpihovsky.feature_settings.main.MainSettingsScreen
import com.varpihovsky.jetiq.NavigationViewModel
import soup.compose.material.motion.*

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
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
        bottomBar = {
            AnimatedVisibility(
                visible = navigationViewModel.data.isNavbarShown.value,
                enter = expandVertically(expandFrom = Alignment.CenterVertically),
                exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                BottomNavigationMenu(
                    selected = navigationViewModel.data.selectedNavbarEntry.value,
                    onClick = {
                        JetNav.getController().let { it1 ->
                            navigationViewModel.onBottomBarButtonClick(
                                it.route,
                                it1
                            )
                        }
                    }, buttons = listOf(
                        BottomNavigationItem.MessagesItem,
                        BottomNavigationItem.ProfileItem,
                    )
                )
            }
        }
    ) { paddingValues ->
        ExceptionProcessor(exceptionEventManager = exceptionEventManager)

        val navigationController = initNavigation(
            navigationViewModel = navigationViewModel,
            eventBus = eventBus
        )

        navigationController.setNavigationCallback(navigationViewModel::onDestinationChange)

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
            composable = { handleBackPress(getViewModel<AuthComponent>()) { Auth(viewModel = it) } }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.newMessage.destination
            composable =
                {
                    handleBackPress(getViewModel<NewMessageComponent>()) {
                        NewMessageScreen(
                            newMessageComponent = it
                        )
                    }
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
                    handleBackPress(getViewModel<ContactsComponent>()) {
                        ContactsScreen(
                            contactsComponent = it
                        )
                    }
                }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThroughIn()
            outAnimation = materialFadeThroughOut()
        }
        entry {
            val entryRoute = NavigationDirections.mainSettings.destination
            composable = {
                handleBackPress(getViewModel<MainSettingsComponent>()) {
                    MainSettingsScreen(mainSettingsComponent = it)
                }
            }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThroughIn()
            outAnimation = materialFadeThroughOut()
        }
        entry {
            val entryRoute = NavigationDirections.aboutSettings.destination
            composable = {
                handleBackPress(getViewModel<AboutSettingsComponent>()) {
                    AboutSettingsScreen(aboutSettingsViewModel = it)
                }
            }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.generalSettings.destination
            composable = {
                handleBackPress(getViewModel<GeneralSettingsComponent>()) {
                    GeneralSettingsScreen(generalSettingsComponent = it)
                }
            }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.markbookSubject.destination
            composable = {
                handleBackPress(getViewModel<MarkbookSubjectComponent>()) {
                    MarkbookSubjectScreen(markbookSubjectComponent = it)
                }
            }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.successSubject.destination
            composable = {
                handleBackPress(getViewModel<SuccessSubjectComponent>()) {
                    SuccessSubjectScreen(successSubjectComponent = it)
                }
            }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.messages.destination
            composable =
                { handleBackPress(getViewModel<MessagesComponent>()) { MessageWall(messagesComponent = it) } }
            route = entryRoute
            entryType = EntryType.Main
            inAnimation = materialSharedAxisXIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisXOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.profile.destination
            applyPaddingValues = false
            composable = { values ->
                handleBackPress(getViewModel<ProfileComponent>()) {
                    Profile(
                        profileComponent = get(),
                        paddingValues = values
                    )
                }
            }
            route = entryRoute
            entryType = EntryType.Main
            inAnimation = materialSharedAxisXIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisXOut(forward = false, slideDistance = 200)
        }
    }

    return JetNav.getController()
}

@Composable
inline fun <reified T : JetIQViewModel> handleBackPress(
    viewModel: T,
    screen: @Composable (T) -> Unit
) {
    BackHandler(true, viewModel::onBackNavButtonClick)
    screen(viewModel)
}

@ExperimentalAnimationApi
@Composable
fun BottomNavigationMenu(
    selected: BottomNavigationItem,
    onClick: (BottomNavigationItem) -> Unit,
    buttons: List<BottomNavigationItem>
) {
    InfoCard(
        modifier = Modifier
            .height(60.dp)
            .border(
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
                )
            ),
        elevation = 15.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttons.forEach {
                BottomNavigationMenuButton(
                    checked = it == selected,
                    painter = painterResource(id = it.iconId),
                    contentDescription = it.subscription,
                    buttonSubscription = it.subscription,
                    onClick = { onClick(it) }
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomNavigationMenuButton(
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