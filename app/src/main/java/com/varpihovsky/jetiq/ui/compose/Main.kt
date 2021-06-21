package com.varpihovsky.jetiq.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.varpihovsky.jetiq.SharedViewModel
import com.varpihovsky.jetiq.screens.auth.Auth
import com.varpihovsky.jetiq.screens.messages.MessagesScreen
import com.varpihovsky.jetiq.screens.profile.Profile
import com.varpihovsky.jetiq.system.navigation.BottomNavigationItem
import com.varpihovsky.jetiq.system.navigation.NavigationDirections

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Root(
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    val startDestination = remember { sharedViewModel.getStartDestination() }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            val isNavbarShown by sharedViewModel.data.isNavbarShown.observeAsState(false)
            val selectedEntry by sharedViewModel.data.selectedNavbarEntry.observeAsState(initial = BottomNavigationItem.ProfileItem)

            if (isNavbarShown) {
                BottomNavigationMenu(
                    selected = selectedEntry,
                    onClick = { sharedViewModel.onBottomBarButtonClick(it.route) },
                    BottomNavigationItem.MessagesItem,
                    BottomNavigationItem.ProfileItem,
                )
            }
        }
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
                Auth(
                    viewModel = hiltViewModel(
                        backStackEntry = navController.getBackStackEntry(
                            NavigationDirections.authentication.destination
                        )
                    )
                )
            }
            composable(
                route = NavigationDirections.profile.destination,
                arguments = NavigationDirections.profile.arguments
            ) {
                Profile(
                    profileViewModel = hiltViewModel(
                        backStackEntry = navController.getBackStackEntry(
                            NavigationDirections.profile.destination
                        )
                    )
                )
            }
            composable(
                route = NavigationDirections.messages.destination,
                arguments = NavigationDirections.messages.arguments
            ) {
                MessagesScreen(
                    viewModel = hiltViewModel(
                        backStackEntry = navController.getBackStackEntry(
                            NavigationDirections.messages.destination
                        )
                    )
                )
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