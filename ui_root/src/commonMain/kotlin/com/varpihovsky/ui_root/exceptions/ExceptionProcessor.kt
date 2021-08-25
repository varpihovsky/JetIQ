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
package com.varpihovsky.ui_root.exceptions

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.varpihovsky.core_ui.compose.ON_EMPTY_EXCEPTION_MESSAGE
import com.varpihovsky.core_ui.compose.foundation.ErrorDialog

@Composable
fun ExceptionProcessor(exceptionComponent: ExceptionComponent) {
    var isExceptionShown by rememberSaveable { mutableStateOf(false) }
    val currentException by exceptionComponent.currentException.collectAsState()

    LaunchedEffect(key1 = currentException) {
        if (currentException?.message == null) {
            return@LaunchedEffect
        }

        isExceptionShown = true
    }

    if (isExceptionShown) {
        ErrorDialog(message = currentException?.message ?: ON_EMPTY_EXCEPTION_MESSAGE) {
            isExceptionShown = false
        }
    }
}