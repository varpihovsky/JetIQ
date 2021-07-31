package com.varpihovsky.jetiq.ui.compose

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_nav.dsl.DisplayNavigation
import com.varpihovsky.core_nav.dsl.rememberNavigationController
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationControllerStorage
import com.varpihovsky.core_nav.navigation.BottomNavigationItem
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.jetiq.NavigationViewModel
import com.varpihovsky.jetiq.screens.auth.Auth
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsScreen
import com.varpihovsky.jetiq.screens.messages.create.NewMessageScreen
import com.varpihovsky.jetiq.screens.messages.main.MessagesScreen
import com.varpihovsky.jetiq.screens.profile.Profile
import com.varpihovsky.jetiq.screens.settings.about.AboutSettingsScreen
import com.varpihovsky.jetiq.screens.settings.general.GeneralSettingsScreen
import com.varpihovsky.jetiq.screens.settings.main.MainSettingsScreen
import com.varpihovsky.jetiq.screens.subjects.markbook.MarkbookSubjectScreen
import com.varpihovsky.jetiq.screens.subjects.success.SuccessSubjectScreen
import soup.compose.material.motion.*

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Root(
    navigationViewModel: NavigationViewModel,
    navigationControllerStorage: NavigationControllerStorage,
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
                        navigationControllerStorage.navigationController?.let { it1 ->
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
        },
        topBar = { Appbar(appbarManager = appbarManager) }

    ) { paddingValues ->
        ExceptionProcessor(exceptionEventManager = exceptionEventManager)

        val navigationController = initNavigation(
            navigationViewModel = navigationViewModel,
            eventBus = eventBus
        )

        navigationController.setNavigationCallback(navigationViewModel::onDestinationChange)

        navigationControllerStorage.navigationController = navigationController

        RootLayout(appbarManager = appbarManager, paddingValues = paddingValues) {
            DisplayNavigation(
                modifier = Modifier.padding(paddingValues = paddingValues),
                controller = navigationController,
                paddingValues = it
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun RootLayout(
    appbarManager: AppbarManager,
    paddingValues: PaddingValues,
    content: @Composable (PaddingValues) -> Unit
) {
    SubcomposeLayout { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(constraints.maxWidth, constraints.maxHeight) {
            val appbar = subcompose(RootLayout.APP_BAR) {
                Appbar(appbarManager = appbarManager)
            }.map { it.measure(looseConstraints) }

            val appbarHeight = appbar.maxByOrNull { it.height }?.height ?: 0

            subcompose(RootLayout.NAV) {
                content(
                    PaddingValues(
                        top = appbarHeight.toDp(),
                        bottom = paddingValues.calculateBottomPadding()
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

    return rememberNavigationController(
        eventBus = eventBus,
        defaultRoute = startDestination
    ) {
        entry {
            val entryRoute = NavigationDirections.authentication.destination
            composable = { Auth(viewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.newMessage.destination
            composable = { NewMessageScreen(newMessageViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.contacts.destination
            composable = { ContactsScreen(contactsViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThroughIn()
            outAnimation = materialFadeThroughOut()
        }
        entry {
            val entryRoute = NavigationDirections.mainSettings.destination
            composable = { MainSettingsScreen(mainSettingsViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThroughIn()
            outAnimation = materialFadeThroughOut()
        }
        entry {
            val entryRoute = NavigationDirections.aboutSettings.destination
            composable =
                { AboutSettingsScreen(aboutSettingsViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.generalSettings.destination
            composable =
                { GeneralSettingsScreen(generalSettingsViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScaleIn()
            outAnimation = materialElevationScaleOut()
        }
        entry {
            val entryRoute = NavigationDirections.markbookSubject.destination
            composable =
                { MarkbookSubjectScreen(markbookSubjectViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.successSubject.destination
            composable =
                { SuccessSubjectScreen(markbookSubjectViewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxisYIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisYOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.messages.destination
            composable = { MessagesScreen(viewModel = viewModel(key = entryRoute)) }
            route = entryRoute
            entryType = EntryType.Main
            inAnimation = materialSharedAxisXIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisXOut(forward = false, slideDistance = 200)
        }
        entry {
            val entryRoute = NavigationDirections.profile.destination
            applyPaddingValues = false
            composable =
                { Profile(profileViewModel = viewModel(key = entryRoute), paddingValues = it) }
            route = entryRoute
            entryType = EntryType.Main
            inAnimation = materialSharedAxisXIn(forward = true, slideDistance = 200)
            outAnimation = materialSharedAxisXOut(forward = false, slideDistance = 200)
        }
    }
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