package com.varpihovsky.core_ui.compose.foundation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.SwipeRefresh as Refresh

@Composable
internal actual fun SwipeRefreshActual(
    modifier: Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    indicatorPadding: PaddingValues,
    content: @Composable () -> Unit
) {
    Refresh(
        modifier = modifier,
        state = SwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        indicatorPadding = indicatorPadding,
        content = content
    )
}