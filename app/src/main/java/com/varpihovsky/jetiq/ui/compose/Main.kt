package com.varpihovsky.jetiq.ui.compose

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.navigation.BottomNavigationItem
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core_nav.dsl.DisplayNavigation
import com.varpihovsky.core_nav.dsl.rememberNavigationController
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationControllerStorage
import com.varpihovsky.jetiq.NavigationViewModel
import com.varpihovsky.jetiq.screens.auth.Auth
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsScreen
import com.varpihovsky.jetiq.screens.messages.create.NewMessageScreen
import com.varpihovsky.jetiq.screens.messages.main.MessagesScreen
import com.varpihovsky.jetiq.screens.profile.Profile
import com.varpihovsky.jetiq.screens.settings.about.AboutSettingsScreen
import com.varpihovsky.jetiq.screens.settings.main.MainSettingsScreen
import soup.compose.material.motion.Axis
import soup.compose.material.motion.materialElevationScale
import soup.compose.material.motion.materialFadeThrough
import soup.compose.material.motion.materialSharedAxis

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

        DisplayNavigation(
            modifier = Modifier.padding(paddingValues = paddingValues),
            controller = navigationController
        )
    }
}

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
            composable =
                { Auth(viewModel = viewModel(key = NavigationDirections.authentication.destination)) }
            route = NavigationDirections.authentication.destination
            entryType = EntryType.SubMenu
            inAnimation = materialSharedAxis(
                axis = Axis.Y,
                forward = true
            )
            outAnimation = materialSharedAxis(
                axis = Axis.Y,
                forward = false
            )
        }
        entry {
            composable =
                { NewMessageScreen(newMessageViewModel = viewModel(key = NavigationDirections.newMessage.destination)) }
            route = NavigationDirections.newMessage.destination
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScale(false)
            outAnimation = materialElevationScale(true)
        }
        entry {
            composable =
                { ContactsScreen(contactsViewModel = viewModel(key = NavigationDirections.contacts.destination)) }
            route = NavigationDirections.contacts.destination
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThrough()
            outAnimation = materialFadeThrough()
        }
        entry {
            composable =
                { MainSettingsScreen(mainSettingsViewModel = viewModel(key = NavigationDirections.mainSettings.destination)) }
            route = NavigationDirections.mainSettings.destination
            entryType = EntryType.SubMenu
            inAnimation = materialFadeThrough()
            outAnimation = materialFadeThrough()
        }
        entry {
            composable =
                { AboutSettingsScreen(aboutSettingsViewModel = viewModel(key = NavigationDirections.aboutSettings.destination)) }
            route = NavigationDirections.aboutSettings.destination
            entryType = EntryType.SubMenu
            inAnimation = materialElevationScale(false)
            outAnimation = materialElevationScale(true)
        }
        entry {
            composable =
                { MessagesScreen(viewModel = viewModel(key = NavigationDirections.messages.destination)) }
            route = NavigationDirections.messages.destination
            entryType = EntryType.Main
            inAnimation = materialSharedAxis(
                axis = Axis.X,
                forward = true
            )
            outAnimation = materialSharedAxis(
                axis = Axis.X,
                forward = false
            )
        }
        entry {
            composable =
                { Profile(profileViewModel = viewModel(key = NavigationDirections.profile.destination)) }
            route = NavigationDirections.profile.destination
            entryType = EntryType.Main
            inAnimation = materialSharedAxis(
                axis = Axis.X,
                forward = true
            )
            outAnimation = materialSharedAxis(
                axis = Axis.X,
                forward = false
            )
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