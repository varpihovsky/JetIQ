package com.varpihovsky.jetiq.screens.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.exceptions.ViewModelWithException
import com.varpihovsky.jetiq.system.exceptions.WrongDataException
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.Validator
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val profileModel: ProfileModel,
    @Named("login_checker") private val loginValidator: Validator<String>,
    @Named("password_checker") private val passwordValidator: Validator<String>,
    private val navigationManager: NavigationManager,
    appbarManager: AppbarManager
) : JetIQViewModel(appbarManager, navigationManager), ViewModelWithException {
    val data by lazy { Data() }
    override val exceptions: MutableStateFlow<Exception?> = MutableStateFlow(null)

    private val login = MutableLiveData("")
    private val password = MutableLiveData("")
    private val passwordHidden = MutableLiveData(true)
    private val progressShown = MutableLiveData(false)

    inner class Data {
        val login: LiveData<String> = this@AuthViewModel.login
        val password: LiveData<String> = this@AuthViewModel.password
        val passwordHidden: LiveData<Boolean> = this@AuthViewModel.passwordHidden
        val progressShown: LiveData<Boolean> = this@AuthViewModel.progressShown
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

        if (!loginValidator.validate(login.value!!) && !passwordValidator.validate(password.value!!)) {
            exceptions.value =
                WrongDataException("Логін або пароль не пройшли перевірку на правильність")
            progressShown.value = false
            return
        }

        viewModelScope.launch(Dispatchers.IO) { processLogin() }
    }

    private fun processLogin() {
        try {
            profileModel.login(login.value!!, password.value!!)
            navigationManager.manage(NavigationDirections.profile)
        } catch (e: Exception) {
            Log.d("Application", Log.getStackTraceString(e))
            exceptions.value = e
        }
        viewModelScope.launch { progressShown.value = false }
    }


}