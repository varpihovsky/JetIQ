package com.varpihovsky.jetiq.ui.compose

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

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.jetiq.screens.JetIQViewModel

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
fun MapLifecycle(viewModel: JetIQViewModel) {
    DisposableEffect(key1 = Unit) {
        viewModel.onCompose()
        onDispose { viewModel.onDispose() }
    }
}

@Composable
fun OpenPage(url: String) {
    startActivity(LocalContext.current, Intent(Intent.ACTION_VIEW, Uri.parse(url)), null)
}