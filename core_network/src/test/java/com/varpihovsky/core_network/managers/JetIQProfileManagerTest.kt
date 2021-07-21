package com.varpihovsky.core_network.managers

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

import com.varpihovsky.core_network.NullableProfile
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_network.result.asSuccess
import com.varpihovsky.core_network.testCore.JetIQNetworkManagerTest
import com.varpihovsky.repo_data.ProfileDTO
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Headers
import org.junit.Test

class JetIQProfileManagerTest : JetIQNetworkManagerTest() {
    private lateinit var jetIQProfileManager: JetIQProfileManager

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        jetIQProfileManager = JetIQProfileManager(jetIQApi)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test login adds session to profile`() = runBlockingTest {
        val headers: Headers = mockk()
        every { headers.get("Set-Cookie") } returns TEST_SESSION

        coEvery { jetIQApi.authorize(any(), any()) } returns Result.Success.HttpResponse(
            TEST_PROFILE_NULLABLE,
            0,
            headers = headers
        )

        assertEquals(
            TEST_PROFILE,
            jetIQProfileManager.login("", "").asSuccess().value
        )
    }

    companion object {
        const val TEST_SESSION = "example"

        val TEST_PROFILE_NULLABLE = NullableProfile(
            1,
            "",
            "",
            null,
            "",
            "",
            "",
            "",
            "",
            "",
            null,
            "",
            ""
        )

        val TEST_PROFILE = ProfileDTO(
            1,
            "",
            "",
            null,
            "",
            "",
            "",
            "",
            "",
            "",
            TEST_SESSION,
            "",
            ""
        )
    }
}