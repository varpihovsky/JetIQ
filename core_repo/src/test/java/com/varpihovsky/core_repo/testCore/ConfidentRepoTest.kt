package com.varpihovsky.core_repo.testCore

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