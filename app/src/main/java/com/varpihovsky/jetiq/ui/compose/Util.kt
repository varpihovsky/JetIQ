package com.varpihovsky.jetiq.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.navigation.NavigationCommandType
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import kotlinx.coroutines.flow.collect


suspend fun collectNavigationCommands(
    navigationManager: NavigationManager,
    currentDestination: () -> String?,
    onDestinationChange: (String) -> Unit,
    navController: NavHostController
) {
    navigationManager.commands.collect {
        if (it.destination.isNotEmpty() && it.destination != currentDestination()) {
            navController.navigate(it.destination) {
                if (it.type == NavigationCommandType.MAIN) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        if (it.type == NavigationCommandType.BACK) {
            navController.popBackStack()
        }
    }
}

@Composable
fun CollectExceptions(
    viewModel: ViewModelWithException
) {
    val exception by viewModel.exceptions.collectAsState()
    exception?.message?.let {
        ErrorDialog(message = it, onDismiss = viewModel::onExceptionProcessed)
    }
}

@Composable
inline fun <reified T : JetIQViewModel> hiltJetIQViewModel(): T {

    return hiltViewModel()
}