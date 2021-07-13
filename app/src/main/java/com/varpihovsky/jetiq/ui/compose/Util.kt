package com.varpihovsky.jetiq.ui.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.core.navigation.NavigationCommandType
import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import kotlinx.coroutines.flow.collect


suspend fun collectNavigationCommands(
    navigationManager: NavigationManager,
    currentDestination: () -> String?,
    onDestinationChange: (String) -> Unit,
    navController: NavHostController
) {
    navigationManager.commands.collect {
        Log.d("Nav", navController.backQueue.toString())

        if (it.destination.isNotEmpty() && it.destination != currentDestination()) {
            navController.navigate(it.destination) {
                if (it.type == NavigationCommandType.MAIN) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive = true
                    }
                } else {
                    launchSingleTop
                    restoreState = true
                    popUpTo(it.destination) {
                        saveState = true
                        inclusive = true
                    }
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
fun MapLifecycle(viewModel: JetIQViewModel) {
    DisposableEffect(key1 = Unit) {
        viewModel.onCompose()
        onDispose { viewModel.onDispose() }
    }
}

@Composable
inline fun <reified T : JetIQViewModel> hiltJetIQViewModel(): T {
    return hiltViewModel()
}