package com.varpihovsky.jetiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.compose.CollectNavigationCommands
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

            CollectNavigationCommands(
                navigationManager = navigationManager,
                currentDestination = viewModel::getCurrentDestination,
                onDestinationChange = viewModel::onDestinationChange,
                navController = navController
            )

            JetIQTheme {
                Root(sharedViewModel = viewModel, navController = navController)
            }
        }
    }

}