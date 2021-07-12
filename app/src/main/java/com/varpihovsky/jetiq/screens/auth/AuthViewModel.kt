package com.varpihovsky.jetiq.screens.auth

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.exceptions.ViewModelWithException
import com.varpihovsky.core.exceptions.WrongDataException
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.jetiq.appbar.AppbarManager
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val profileModel: ProfileRepo,
    @Named("login_checker") private val loginValidator: Validator<String>,
    @Named("password_checker") private val passwordValidator: Validator<String>,
    private val navigationManager: NavigationManager,
    appbarManager: AppbarManager,
) : JetIQViewModel(appbarManager, navigationManager), ViewModelWithException {
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
        try {
            authorize()
        } catch (e: Exception) {
            redirectExceptionToUI(e)
        }
    }

    private fun authorize() {
        profileModel.login(login.value, password.value)
        navigationManager.manage(NavigationDirections.profile)
    }
}