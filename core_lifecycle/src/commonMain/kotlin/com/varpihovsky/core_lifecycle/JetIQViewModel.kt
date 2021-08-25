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
package com.varpihovsky.core_lifecycle

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.varpihovsky.core.appbar.AppbarCommand
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.lifecycle.ViewModel
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.ThreadSafeMutableState
import com.varpihovsky.core_nav.main.NavigationController

abstract class JetIQViewModel(
    val appbarManager: AppbarManager,
    open val navigationController: NavigationController,
    val exceptionEventManager: ExceptionEventManager
) : ViewModel() {
    open fun onBackNavButtonClick() {
        navigationController.onBack()
    }
}

fun JetIQViewModel.assignAppbar(
    title: String? = null,
    icon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    appbarManager.manage(
        AppbarCommand.Configured(
            title = title,
            navIcon = icon,
            actions = actions
        )
    )
}

fun JetIQViewModel.assignAppbar(bar: @Composable () -> Unit) {
    appbarManager.manage(AppbarCommand.Custom(bar))
}

fun JetIQViewModel.emptyAppbar() {
    appbarManager.manage(AppbarCommand.Empty)
}

fun JetIQViewModel.redirectExceptionToUI(exception: Exception) {
    exceptionEventManager.pushException(exception)
}

fun <T> JetIQViewModel.mutableStateOf(t: T): ThreadSafeMutableState<T> {
    return ThreadSafeMutableState(t, viewModelScope)
}