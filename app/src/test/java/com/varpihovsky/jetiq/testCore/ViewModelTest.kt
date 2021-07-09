package com.varpihovsky.jetiq.testCore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

abstract class ViewModelTest : JetIQTest() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    protected var appbarManager: AppbarManager = mockk(relaxed = true)
    protected var navigationManager: NavigationManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    protected var viewModelDispatchers = CoroutineDispatchers(TestCoroutineDispatcher())

    @ExperimentalCoroutinesApi
    private var testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    open fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @ExperimentalCoroutinesApi
    override fun teardown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        super.teardown()
    }
}