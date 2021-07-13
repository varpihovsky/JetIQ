package com.varpihovsky.core.testCore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class CoroutineTest : JetIQTest() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
}