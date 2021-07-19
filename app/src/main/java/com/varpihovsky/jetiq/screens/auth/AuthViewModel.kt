package com.varpihovsky.jetiq.screens.auth

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.exceptions.WrongDataException
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val profileRepo: ProfileRepo,
    @Named("login_checker") private val loginValidator: Validator<String>,
    @Named("password_checker") private val passwordValidator: Validator<String>,
    private val navigationController: NavigationController,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val data by lazy { Data() }

    private var login = mutableStateOf("")
    private val password = mutableStateOf("")
    private val passwordHidden = mutableStateOf(true)
    private val progressShown = mutableStateOf(false)

    inner class Data {
        val login: State<String> = this@AuthViewModel.login
        val password: State<String> = this@AuthViewModel.password
        val passwordHidden: State<Boolean> = this@AuthViewModel.passwordHidden
        val progressShown: State<Boolean> = this@AuthViewModel.progressShown
    }

    fun onLoginChange(value: String) {
        login.value = value
    }

    fun onPasswordChange(value: String) {
        password.value = value
    }

    fun onPasswordHiddenChange(value: Boolean) {
        passwordHidden.value = value
    }

    fun onLogin() {
        progressShown.value = true

        if (isInputInvalid()) {
            progressShown.value = false
            return
        }

        viewModelScope.launch(dispatchers.IO) {
            processLogin()
            progressShown.value = false
        }
    }

    private fun isInputInvalid(): Boolean =
        if (!loginValidator.validate(login.value) && !passwordValidator.validate(password.value)) {
            redirectExceptionToUI(WrongDataException(Values.LOGIN_OR_PASS_IS_NOT_RIGHT))
            true
        } else {
            false
        }

    private fun processLogin() {
        viewModelScope.launch(dispatchers.IO) { authorize() }
    }

    private suspend fun authorize() {
        if (profileRepo.login(login.value, password.value)) {
            navigationController.manage(NavigationDirections.profile.destination)
        }
    }
}