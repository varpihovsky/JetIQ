package com.varpihovsky.jetiq.testCore

import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.jetiq.appbar.AppbarManager
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before

abstract class ViewModelTest : JetIQTest() {


    protected var appbarManager: AppbarManager = mockk(relaxed = true)
    protected var navigationController: NavigationController = mockk(relaxed = true)

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