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
package com.varpihovsky.core_ui.compose

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_ui.compose.foundation.ErrorDialog

@Composable
fun ExceptionProcessor(exceptionEventManager: ExceptionEventManager) {
    val isExceptionShown = rememberSaveable { mutableStateOf(false) }
    val currentException = exceptionEventManager.exceptions.collectAsState(
        initial = Throwable(EMPTY_EXCEPTION_ID) //It could be sealed class, but IMO there is no diff
    )

    LaunchedEffect(key1 = currentException.value) {
        if (currentException.value.message == EMPTY_EXCEPTION_ID) {
            return@LaunchedEffect
        }

        isExceptionShown.value = true
    }

    if (isExceptionShown.value) {
        ErrorDialog(message = currentException.value.message ?: ON_EMPTY_EXCEPTION_MESSAGE) {
            isExceptionShown.value = false
        }
    }
}

const val EMPTY_EXCEPTION_ID = "EMPTY"
const val ON_EMPTY_EXCEPTION_MESSAGE = "Невідома помилка!"

@Composable
expect fun OpenPage(url: String)