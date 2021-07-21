package com.varpihovsky.core_repo.testCore

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

import com.varpihovsky.core_db.dao.ConfidentialDAO
import com.varpihovsky.core_db.dao.ProfileDAO
import com.varpihovsky.repo_data.Confidential
import com.varpihovsky.repo_data.ProfileDTO
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow

open class ConfidentRepoTest : CoroutineTest() {
    protected val confidentialDAO: ConfidentialDAO = mockk(relaxed = true)
    protected val profileDAO: ProfileDAO = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        every { confidentialDAO.get() } returns flow { emit(TEST_CONFIDENTIAL) }
        every { profileDAO.get() } returns flow { emit(TEST_PROFILE) }
    }

    companion object {
        const val TEST_LOGIN = "login"
        const val TEST_PASSWORD = "password"

        val TEST_CONFIDENTIAL = Confidential(TEST_LOGIN, TEST_PASSWORD)

        val TEST_PROFILE = ProfileDTO(
            1,
            "1",
            "Example",
            null,
            "email@example.com",
            "1",
            "1",
            "Example group",
            "1",
            "",
            "PHP_SESSION:EXAMPLE",
            "Example spec",
            "Example Student"
        )
    }
}