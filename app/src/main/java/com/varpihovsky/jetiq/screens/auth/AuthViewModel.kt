package com.varpihovsky.jetiq.screens.auth

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.exceptions.Values
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.exceptions.WrongDataException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
import com.varpihovsky.jetiq.system.util.Validator
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val profileModel: ProfileModel,
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