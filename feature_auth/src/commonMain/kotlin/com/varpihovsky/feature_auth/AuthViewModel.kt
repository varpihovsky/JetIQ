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
package com.varpihovsky.feature_auth

import androidx.compose.runtime.State
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_lifecycle.mutableStateOf
import com.varpihovsky.core_lifecycle.redirectExceptionToUI
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val profileRepo: ProfileRepo,
    private val subjectRepo: SubjectRepo,
    private val messagesRepo: MessagesRepo,
    private val loginValidator: Validator,
    private val passwordValidator: Validator,
    override val navigationController: NavigationController,
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
        (!loginValidator.validate(login.value) && !passwordValidator.validate(password.value)).also {
            // if invalid - show on ui.
            if (it) {
                redirectExceptionToUI(RuntimeException(Values.LOGIN_OR_PASS_IS_NOT_RIGHT))
            }
        }

    private suspend fun processLogin() {
        authorize()
    }

    private suspend fun authorize() {
        if (profileRepo.login(login.value, password.value)) {
            viewModelScope.launch(dispatchers.IO) { subjectRepo.load() }
            viewModelScope.launch(dispatchers.IO) { messagesRepo.loadMessages() }
            navigationController.manage(NavigationDirections.profile.destination)
        }
    }
}