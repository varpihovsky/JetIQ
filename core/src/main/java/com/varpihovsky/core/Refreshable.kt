package com.varpihovsky.core

import androidx.compose.runtime.State

interface Refreshable {
    val isLoading: State<Boolean>

    fun onRefresh()
}