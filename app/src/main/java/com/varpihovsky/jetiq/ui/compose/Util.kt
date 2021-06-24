package com.varpihovsky.jetiq.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.varpihovsky.jetiq.system.navigation.NavigationManager

@Composable
fun CollectNavigationCommands(
    navigationManager: NavigationManager,
    currentDestination: () -> String?,
    onDestinationChange: (String) -> Unit,
    navController: NavHostController
) {
    navigationManager.commands.collectAsState().value.also {
        if (it.destination.isNotEmpty() && it.destination != currentDestination()) {
            onDestinationChange(it.destination)
            navController.navigate(it.destination) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}