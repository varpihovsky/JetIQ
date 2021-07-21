package com.varpihovsky.jetiq.screens.settings.about

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

import androidx.compose.runtime.State
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSettingsViewModel @Inject constructor(
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }

    private val pageToOpen = mutableStateOf("")

    inner class Data {
        val pageToOpen: State<String> = this@AboutSettingsViewModel.pageToOpen
    }

    fun onUrlButtonClick(url: String) {
        pageToOpen.value = url
    }

    fun onPageOpened() {
        pageToOpen.value = ""
    }
}