package com.varpihovsky.core_test

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

actual open class CoroutineTest actual constructor() : JetIQTest() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) =
        kotlinx.coroutines.test.runBlockingTest {
            block(this)
        }

    @ExperimentalCoroutinesApi
    @Before
    @CallSuper
    actual override fun setup() {
        Dispatchers.setMain(testDispatcher as CoroutineDispatcher)
    }
}