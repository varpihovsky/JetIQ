package com.varpihovsky.core_repo.repo

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

import com.varpihovsky.core_network.managers.JetIQProfileManager
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_repo.testCore.ConfidentRepoTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileRepoTest : ConfidentRepoTest() {
    private lateinit var profileRepo: ProfileRepo

    private val jetIQProfileManager: JetIQProfileManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        profileRepo = ProfileRepo(
            profileDAO,
            confidentialDAO,
            jetIQProfileManager,
            exceptionEventManager
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_login_resets_confidential_and_profile() = runBlockingTest {
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
    fun test_logout() = runBlockingTest {
        profileRepo.logout()
        coVerify { jetIQProfileManager.logout(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getProfile() = runBlockingTest {
        assertEquals(TEST_PROFILE, profileRepo.getProfile().last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getConfidential() = runBlockingTest {
        assertEquals(TEST_CONFIDENTIAL, profileRepo.getConfidential().last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_clear_clears_databases() = runBlockingTest {
        profileRepo.clear()
        verifyAll {
            profileDAO.delete()
            confidentialDAO.delete()
        }
    }
}