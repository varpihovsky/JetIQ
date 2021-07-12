package com.varpihovsky.jetiq.ui.compose

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.varpihovsky.core.navigation.BottomNavigationItem
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.jetiq.NavigationViewModel
import com.varpihovsky.jetiq.appbar.AppbarCommand
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.auth.Auth
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsScreen
import com.varpihovsky.jetiq.screens.messages.create.NewMessageScreen
import com.varpihovsky.jetiq.screens.messages.main.MessagesScreen
import com.varpihovsky.jetiq.screens.profile.Profile
import com.varpihovsky.jetiq.screens.settings.about.AboutSettingsScreen
import com.varpihovsky.jetiq.screens.settings.main.MainSettingsScreen

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Root(
    navigationViewModel: NavigationViewModel,
    navController: NavHostController,
    appbarManager: AppbarManager
) {
    val startDestination = remember { navigationViewModel.getStartDestination() }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            AnimatedVisibility(
                visible = navigationViewModel.data.isNavbarShown.value,
                enter = expandIn(expandFrom = Alignment.BottomCenter),
                exit = shrinkOut(shrinkTowards = Alignment.TopCenter)
            ) {
                BottomNavigationMenu(
                    selected = navigationViewModel.data.selectedNavbarEntry.value,
                    onClick = { navigationViewModel.onBottomBarButtonClick(it.route) },
                    BottomNavigationItem.MessagesItem,
                    BottomNavigationItem.ProfileItem,
                )
            }
        },
        topBar = appbarManager.commands.collectAsState(AppbarCommand { }).value.bar

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "Parent",
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            navigation(startDestination = startDestination, route = "Parent") {
                composable(
                    route = NavigationDirections.authentication.destination,
                    arguments = NavigationDirections.authentication.arguments
                ) {
                    Auth(viewModel = hiltViewModel(navController.getBackStackEntry("Parent")))
                }
                composable(
                    route = NavigationDirections.profile.destination,
                    arguments = NavigationDirections.profile.arguments
                ) {
                    Profile(profileViewModel = hiltViewModel(navController.getBackStackEntry("Parent")))
                }
                composable(
                    route = NavigationDirections.messages.destination,
                    arguments = NavigationDirections.messages.arguments
                ) {
                    MessagesScreen(viewModel = hiltViewModel(navController.getBackStackEntry("Parent")))
                }
                composable(
                    route = NavigationDirections.contacts.destination,
                    arguments = NavigationDirections.contacts.arguments
                ) {
                    ContactsScreen(
                        contactsViewModel = hiltViewModel(
                            navController.getBackStackEntry(
                                "Parent"
                            )
                        )
                    )
                }
                composable(
                    route = NavigationDirections.newMessage.destination,
                    arguments = NavigationDirections.newMessage.arguments
                ) {
                    NewMessageScreen(
                        newMessageViewModel = hiltViewModel(navController.getBackStackEntry("Parent"))
                    )
                }
                composable(
                    route = NavigationDirections.mainSettings.destination,
                    arguments = NavigationDirections.mainSettings.arguments
                ) {
                    MainSettingsScreen(
                        mainSettingsViewModel = hiltViewModel(navController.getBackStackEntry("Parent"))
                    )
                }
                composable(
                    route = NavigationDirections.aboutSettings.destination,
                    arguments = NavigationDirections.aboutSettings.arguments
                ) {
                    AboutSettingsScreen(
                        aboutSettingsViewModel = hiltViewModel(navController.getBackStackEntry("Parent"))
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomNavigationMenu(
    selected: BottomNavigationItem,
    onClick: (BottomNavigationItem) -> Unit,
    vararg buttons: BottomNavigationItem
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