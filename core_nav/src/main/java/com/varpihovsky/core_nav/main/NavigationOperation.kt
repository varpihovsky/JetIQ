package com.varpihovsky.core_nav.main

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import soup.compose.material.motion.MotionSpec
import soup.compose.material.motion.materialFade

internal sealed class NavigationOperation {

    class Navigate(
        val route: String = "",
        val composable: @Composable () -> Unit = {},
        val motionSpec: MotionSpec = materialFade(),
    ) : NavigationOperation()

    class Finish(private val onFinished: () -> Unit) : NavigationOperation() {
        private var isProcessed = false

        @SuppressLint("ComposableNaming")
        @Composable
        fun process(block: @Composable () -> Unit) {
            if (!isProcessed) {
                onFinished()
                block()
                isProcessed = true
            }
        }
    }
}