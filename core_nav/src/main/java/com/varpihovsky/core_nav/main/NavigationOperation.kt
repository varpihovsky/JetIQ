package com.varpihovsky.core_nav.main

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import soup.compose.material.motion.MotionSpec
import soup.compose.material.motion.materialFadeThroughIn
import soup.compose.material.motion.materialFadeThroughOut
import soup.compose.material.motion.with

internal sealed class NavigationOperation {

    @ExperimentalAnimationApi
    data class Navigate(
        val route: String = "",
        val composable: @Composable (PaddingValues) -> Unit = {},
        val animation: MotionSpec = materialFadeThroughIn() with materialFadeThroughOut(),
        val pop: Boolean = false
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