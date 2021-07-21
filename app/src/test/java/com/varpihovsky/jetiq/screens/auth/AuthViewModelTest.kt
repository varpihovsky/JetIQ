package com.varpihovsky.jetiq.screens.auth

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

import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class AuthViewModelTest : ViewModelTest() {
    private lateinit var viewModel: AuthViewModel
    private val profileModel: ProfileRepo = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    private val dispatchers: CoroutineDispatchers = CoroutineDispatchers(TestCoroutineDispatcher())

    private val stubValidator: Validator<String> = object : Validator<String> {
        override fun validate(t: String): Boolean = t.isNotEmpty()
    }

    @ExperimentalCoroutinesApi
    @Before
    override fun setup() {
        super.setup()
        viewModel = AuthViewModel(
            dispatchers,
            profileModel,
            stubValidator,
            stubValidator,
            navigationController,
            appbarManager,
            exceptionEventManager
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test login is changing`() = runBlockingTest {
        viewModel.onLoginChange(SAMPLE_LOGIN)
        assertEquals(viewModel.data.login.value, SAMPLE_LOGIN)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test password is changing`() = runBlockingTest {
        viewModel.onPasswordChange(SAMPLE_PASSWORD)
        assertEquals(viewModel.data.password.value, SAMPLE_PASSWORD)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test passwordHidden is changing`() = runBlockingTest {
        viewModel.onPasswordHiddenChange(false)
        assertFalse(viewModel.data.passwordHidden.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test validations are processed`() = runBlockingTest {
        authorize(EMPTY, EMPTY)
        verify { exceptionEventManager.pushException(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test navigate to profile when auth is successful`() = runBlockingTest {
        coEvery { profileModel.login(SAMPLE_LOGIN, SAMPLE_PASSWORD) } returns true
        authorize(SAMPLE_LOGIN, SAMPLE_PASSWORD)
        verify(exactly = 1) { navigationController.manage(NavigationDirections.profile.destination) }
        assertFalse(viewModel.data.progressShown.value)
    }

    private fun authorize(login: String, password: String) {
        viewModel.onLoginChange(login)
        viewModel.onPasswordChange(password)
        viewModel.onLogin()
    }

    companion object {
        private const val SAMPLE_LOGIN = "sampleLogin"
        private const val SAMPLE_PASSWORD = "samplePassword"

        private const val EMPTY = ""
    }
}