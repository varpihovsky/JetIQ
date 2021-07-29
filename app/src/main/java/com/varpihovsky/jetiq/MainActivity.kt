package com.varpihovsky.jetiq

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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_nav.main.NavigationControllerStorage
import com.varpihovsky.jetiq.ui.compose.Root
import com.varpihovsky.jetiq.ui.theme.JetIQTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appbarManager: AppbarManager

    @Inject
    lateinit var viewModelDataTransferManager: ViewModelDataTransferManager

    @Inject
    lateinit var navigationControllerStorage: NavigationControllerStorage

    @Inject
    lateinit var exceptionEventManager: ExceptionEventManager

    @Inject
    lateinit var eventBus: EventBus

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = viewModel<NavigationViewModel>(key = VIEW_MODEL_TAG)

            JetIQTheme {
                Root(
                    navigationViewModel = viewModel,
                    appbarManager = appbarManager,
                    navigationControllerStorage = navigationControllerStorage,
                    exceptionEventManager = exceptionEventManager,
                    eventBus = eventBus
                )
            }
        }
    }

    companion object {
        private const val VIEW_MODEL_TAG = "NavigationViewModel"
    }
}