package com.varpihovsky.jetiq.testCore

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
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
    protected var exceptionEventManager: ExceptionEventManager = mockk(relaxed = true)

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