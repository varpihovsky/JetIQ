package com.varpihovsky.core

import androidx.compose.runtime.State

interface Refreshable {
    /**
     * State that has true value if content is loading.
     */
    val isLoading: State<Boolean>

    /**
     * Method that will be invoked when user refreshes any data.
     */
    fun onRefresh()
}