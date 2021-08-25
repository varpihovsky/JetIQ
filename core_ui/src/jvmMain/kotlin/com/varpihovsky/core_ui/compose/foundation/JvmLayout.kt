package com.varpihovsky.core_ui.compose.foundation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun SwipeRefreshActual(
    modifier: Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    indicatorPadding: PaddingValues,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}