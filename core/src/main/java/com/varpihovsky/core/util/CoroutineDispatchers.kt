package com.varpihovsky.core.util

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Class that used for dispatcher injection. Needed for tests.
 *
 * @author Vladyslav Podrezenko
 */
data class CoroutineDispatchers(
    val IO: CoroutineDispatcher
)
