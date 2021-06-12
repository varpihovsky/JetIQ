package com.varpihovsky.jetiq.ui.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.varpihovsky.jetiq.SharedViewModel
import com.varpihovsky.jetiq.auth.Auth
import com.varpihovsky.jetiq.system.navigation.NavigationDirections

@Composable
fun Root(
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = sharedViewModel.getStartDestination()
    ) {
        composable(
            route = NavigationDirections.authentication.destination,
            arguments = NavigationDirections.authentication.arguments
        ){
            Auth(viewModel = hiltViewModel())
        }
    }
}