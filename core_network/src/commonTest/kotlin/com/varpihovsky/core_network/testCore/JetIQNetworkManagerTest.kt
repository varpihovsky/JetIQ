package com.varpihovsky.core_network.testCore

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

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.multiplatform.ResponseBodyImpl
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_test.CoroutineTest
import io.mockk.mockk

open class JetIQNetworkManagerTest : CoroutineTest() {
    protected val jetIQApi: JetIQApi = mockk(relaxed = true)
    protected val responseBody: ResponseBodyImpl = mockk(relaxed = true)

    companion object {
        val RESULT_FAILURE = Result.Failure.Error(RuntimeException())
    }
}