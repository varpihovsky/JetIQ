package com.varpihovsky.jetiq.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.screens.JetIQViewModel


@Composable
fun CollectExceptions(
    viewModel: ViewModelWithException
) {
    val exception by viewModel.exceptions.collectAsState()
    exception?.message?.let {
        ErrorDialog(message = it, onDismiss = viewModel::onExceptionProcessed)
    }
}

@Composable
fun MapLifecycle(viewModel: JetIQViewModel) {
    DisposableEffect(key1 = Unit) {
        viewModel.onCompose()
        onDispose { viewModel.onDispose() }
    }
}