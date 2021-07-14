package com.varpihovsky.jetiq.screens.auth

import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.*
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
            appbarManager
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
        assertTrue(viewModel.exceptions.value != null)
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