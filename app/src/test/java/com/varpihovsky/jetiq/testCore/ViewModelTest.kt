package com.varpihovsky.jetiq.testCore

import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.NavigationController
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    protected suspend fun <T> Flow<T>.takeLast(initial: T, scope: CoroutineScope): T {
        var result = initial

        val job = scope.launch {
            collectLatest {
                result = it
            }
        }

        job.cancel()
        return result
    }

    @ExperimentalCoroutinesApi
    override fun teardown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        super.teardown()
    }
}