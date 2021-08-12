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
package com.varpihovsky.jetiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.di.get
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_ui.theme.JetIQTheme
import com.varpihovsky.jetiq.ui.Root
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val appbarManager: AppbarManager by inject()
    private val exceptionEventManager: ExceptionEventManager by inject()
    private val eventBus: EventBus by inject()

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = get<NavigationViewModel>()

            JetIQTheme {
                Root(
                    navigationViewModel = viewModel,
                    appbarManager = appbarManager,
                    exceptionEventManager = exceptionEventManager,
                    eventBus = eventBus
                )
            }
        }
    }
}