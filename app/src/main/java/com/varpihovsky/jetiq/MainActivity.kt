package com.varpihovsky.jetiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
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

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            navigationManager.commands.collectAsState().value.also {
                if (it.destination.isNotEmpty()) navController.navigate(it.destination)
            }

            JetIQTheme {
                Root(sharedViewModel = hiltViewModel(), navController = navController)
            }
        }
    }

}