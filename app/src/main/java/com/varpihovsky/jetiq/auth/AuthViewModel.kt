package com.varpihovsky.jetiq.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.exceptions.WrongDataException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.Checker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val profileModel: ProfileModel,
    @Named("login_checker") private val loginChecker: Checker<String>,
    @Named("password_checker") private val passwordChecker: Checker<String>,
    private val navigationManager: NavigationManager
) : ViewModel() {
    val data by lazy { Data() }

    private val login = MutableLiveData("")
    private val password = MutableLiveData("")
    private val passwordHidden = MutableLiveData(true)
    private val progressShown = MutableLiveData(false)
    private val exceptions = MutableStateFlow<Exception?>(null)

    inner class Data {
        val login: LiveData<String> = this@AuthViewModel.login
        val password: LiveData<String> = this@AuthViewModel.password
        val passwordHidden: LiveData<Boolean> = this@AuthViewModel.passwordHidden
        val progressShown: LiveData<Boolean> = this@AuthViewModel.progressShown
        val exceptions: StateFlow<Exception?> = this@AuthViewModel.exceptions
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

        if (!loginChecker.check(login.value!!) && !passwordChecker.check(password.value!!)) {
            throw WrongDataException("Логін або пароль не пройшли перевірку на правильність")
        }

        viewModelScope.launch(Dispatchers.IO) {
            processLogin()
            navigationManager.navigate(NavigationDirections.profile)
        }
    }

    private fun processLogin() {
        try {
            profileModel.login(login.value!!, password.value!!)
        } catch (e: Exception) {
            exceptions.value = e
        }
        progressShown.value = false
    }
}