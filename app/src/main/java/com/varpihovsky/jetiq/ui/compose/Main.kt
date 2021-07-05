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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.varpihovsky.jetiq.SharedViewModel
import com.varpihovsky.jetiq.screens.auth.Auth
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsScreen
import com.varpihovsky.jetiq.screens.messages.main.MessagesScreen
import com.varpihovsky.jetiq.screens.messages.new.NewMessageScreen
import com.varpihovsky.jetiq.screens.profile.Profile
import com.varpihovsky.jetiq.screens.settings.about.AboutSettingsScreen
import com.varpihovsky.jetiq.screens.settings.main.MainSettingsScreen
import com.varpihovsky.jetiq.system.navigation.BottomNavigationItem
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.ui.appbar.AppbarCommand
import com.varpihovsky.jetiq.ui.appbar.AppbarManager

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Root(
    sharedViewModel: SharedViewModel,
    navController: NavHostController,
    appbarManager: AppbarManager,
) {
    val startDestination = remember { sharedViewModel.getStartDestination() }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            val isNavbarShown by sharedViewModel.data.isNavbarShown.observeAsState(false)
            val selectedEntry by sharedViewModel.data.selectedNavbarEntry.observeAsState(initial = BottomNavigationItem.ProfileItem)

            AnimatedVisibility(
                visible = isNavbarShown,
                enter = expandIn(expandFrom = Alignment.BottomCenter),
                exit = shrinkOut(shrinkTowards = Alignment.TopCenter)
            ) {
                BottomNavigationMenu(
                    selected = selectedEntry,
                    onClick = { sharedViewModel.onBottomBarButtonClick(it.route) },
                    BottomNavigationItem.MessagesItem,
                    BottomNavigationItem.ProfileItem,
                )
            }
        },
        topBar = appbarManager.commands.collectAsState(AppbarCommand { }).value.bar

    ) {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues = it)
        ) {
            composable(
                route = NavigationDirections.authentication.destination,
                arguments = NavigationDirections.authentication.arguments
            ) {
                Auth(viewModel = hiltJetIQViewModel())
            }
            composable(
                route = NavigationDirections.profile.destination,
                arguments = NavigationDirections.profile.arguments
            ) {
                Profile(profileViewModel = hiltJetIQViewModel())
            }
            composable(
                route = NavigationDirections.messages.destination,
                arguments = NavigationDirections.messages.arguments
            ) {
                MessagesScreen(viewModel = hiltJetIQViewModel())
            }
            composable(
                route = NavigationDirections.contacts.destination,
                arguments = NavigationDirections.contacts.arguments
            ) {
                ContactsScreen(contactsViewModel = hiltJetIQViewModel())
            }
            composable(
                route = NavigationDirections.newMessage.destination,
                arguments = NavigationDirections.newMessage.arguments
            ) {
                NewMessageScreen(newMessageViewModel = hiltJetIQViewModel())
            }
            composable(
                route = NavigationDirections.mainSettings.destination,
                arguments = NavigationDirections.mainSettings.arguments
            ) {
                MainSettingsScreen(mainSettingsViewModel = hiltJetIQViewModel())
            }
            composable(
                route = NavigationDirections.aboutSettings.destination,
                arguments = NavigationDirections.aboutSettings.arguments
            ) {
                AboutSettingsScreen(aboutSettingsViewModel = hiltJetIQViewModel())
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