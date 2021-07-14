package com.varpihovsky.core_nav.main

import androidx.compose.runtime.Composable
import soup.compose.material.motion.MotionSpec
import soup.compose.material.motion.materialFade

class NavigationOperation(
    val route: String = "",
    val composable: @Composable () -> Unit = {},
    val motionSpec: MotionSpec = materialFade(),
    val type: OperationType = OperationType.Navigate
)