package com.varpihovsky.core_nav.main

import androidx.compose.runtime.Composable
import soup.compose.material.motion.MotionSpec

class NavigationEntry(
    val composable: @Composable () -> Unit,
    val route: String,
    val type: EntryType,
    val inAnimation: MotionSpec,
    val outAnimation: MotionSpec
)

