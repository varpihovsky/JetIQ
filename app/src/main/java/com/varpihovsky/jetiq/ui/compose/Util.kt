package com.varpihovsky.jetiq.ui.compose

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