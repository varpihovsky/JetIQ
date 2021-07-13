package com.varpihovsky.core_network.managers

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
            TEST_PROFILE,
            0,
            headers = headers
        )

        assertEquals(
            TEST_PROFILE.copy(session = TEST_SESSION),
            jetIQProfileManager.login("", "").asSuccess().value
        )
    }

    companion object {
        const val TEST_SESSION = "example"

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
            null,
            "",
            ""
        )
    }
}