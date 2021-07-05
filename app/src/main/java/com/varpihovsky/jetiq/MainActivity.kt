package com.varpihovsky.jetiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.compose.Root
import com.varpihovsky.jetiq.ui.theme.JetIQTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationManager: NavigationManager

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = hiltViewModel<SharedViewModel>()

            navigationManager.commands.collectAsState().value.also {
                if (it.destination.isNotEmpty() && it.destination != viewModel.getCurrentDestination()) {
                    viewModel.onDestinationChange(it.destination)
                    navController.navigate(it.destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }

            JetIQTheme {
                Root(sharedViewModel = viewModel, navController = navController)
            }
        }
    }

}