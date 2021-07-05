package com.varpihovsky.jetiq.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.varpihovsky.jetiq.SharedViewModel
import com.varpihovsky.jetiq.screens.auth.Auth
import com.varpihovsky.jetiq.screens.profile.Profile
import com.varpihovsky.jetiq.system.navigation.NavigationDirections

@ExperimentalAnimationApi
@Composable
fun Root(
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    val startDestination = remember { sharedViewModel.getStartDestination() }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = NavigationDirections.authentication.destination,
            arguments = NavigationDirections.authentication.arguments
        ) {
            Auth(viewModel = hiltViewModel())
        }
        composable(
            route = NavigationDirections.profile.destination,
            arguments = NavigationDirections.profile.arguments
        ) {
            Profile(profileViewModel = hiltViewModel())
        }
    }
}