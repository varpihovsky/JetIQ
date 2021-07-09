package com.varpihovsky.jetiq.system

import androidx.compose.runtime.State

interface Refreshable {
    val isLoading: State<Boolean>

    fun onRefresh()
}