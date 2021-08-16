package com.varpihovsky.core_test

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

actual open class CoroutineTest actual constructor() : JetIQTest() {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) =
        kotlinx.coroutines.test.runBlockingTest {
            block(this)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    @CallSuper
    actual override fun setup() {
        Dispatchers.setMain(testDispatcher as CoroutineDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    @CallSuper
    actual override fun teardown() {
        super.teardown()
        Dispatchers.resetMain()
    }
}