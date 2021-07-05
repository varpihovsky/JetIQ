package com.varpihovsky.jetiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.compose.Root
import com.varpihovsky.jetiq.ui.compose.collectNavigationCommands
import com.varpihovsky.jetiq.ui.theme.JetIQTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var appbarManager: AppbarManager

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = hiltViewModel<SharedViewModel>()
            val coroutineScope = rememberCoroutineScope()
            navController.addOnDestinationChangedListener { _, destination, _ ->
                destination.route?.let { viewModel.onDestinationChange(it) }
            }

            coroutineScope.launch {
                collectNavigationCommands(
                    navigationManager = navigationManager,
                    currentDestination = viewModel::getCurrentDestination,
                    onDestinationChange = viewModel::onDestinationChange,
                    navController = navController
                )
            }

            JetIQTheme {
                Root(
                    sharedViewModel = viewModel,
                    navController = navController,
                    appbarManager = appbarManager
                )
            }
        }
    }
}