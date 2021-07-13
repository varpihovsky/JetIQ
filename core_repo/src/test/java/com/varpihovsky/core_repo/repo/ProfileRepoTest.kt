package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_repo.testCore.ConfidentRepoTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verifyAll
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ProfileRepoTest : ConfidentRepoTest() {
    private lateinit var profileRepo: ProfileRepo

    private val jetIQProfileManager: JetIQProfileManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        profileRepo = ProfileRepo(
            profileDAO,
            confidentialDAO,
            jetIQProfileManager
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test login resets confidential and profile`() = runBlockingTest {
        coEvery { jetIQProfileManager.login(TEST_LOGIN, TEST_PASSWORD) } returns Result.success(
            TEST_PROFILE
        )
        profileRepo.login(TEST_LOGIN, TEST_PASSWORD)
        verifyAll {
            confidentialDAO.delete()
            confidentialDAO.set(TEST_CONFIDENTIAL)
            profileDAO.delete()
            profileDAO.set(TEST_PROFILE)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test logout`() = runBlockingTest {
        profileRepo.logout()
        coVerify { jetIQProfileManager.logout() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getProfile`() = runBlockingTest {
        assertEquals(TEST_PROFILE, profileRepo.getProfile().last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getConfidential`() = runBlockingTest {
        assertEquals(TEST_CONFIDENTIAL, profileRepo.getConfidential().last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test clear clears databases`() = runBlockingTest {
        profileRepo.clear()
        verifyAll {
            profileDAO.delete()
            confidentialDAO.delete()
        }
    }
}