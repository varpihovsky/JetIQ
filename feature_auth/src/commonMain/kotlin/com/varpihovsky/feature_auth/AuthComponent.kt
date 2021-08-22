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

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.varpihovsky.core.exceptions.Values
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.Validator
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.qualifier

class AuthComponent(
    jetIQComponentContext: JetIQComponentContext,
) : JetIQComponentContext by jetIQComponentContext, KoinComponent, Lifecycle.Callbacks {
    val login: Value<String> by lazy { _login }
    val password: Value<String> by lazy { _password }
    val isPasswordHidden: Value<Boolean> by lazy { _isPasswordHidden }
    val isProgressShown: Value<Boolean> by lazy { _isProgressShown }

    private val dispatchers: CoroutineDispatchers by inject()
    private val loginValidator: Validator by inject(qualifier = qualifier("login_checker"))
    private val passwordValidator: Validator by inject(qualifier = qualifier("password_checker"))
    private val profileRepo: ProfileRepo by inject()
    private val subjectRepo: SubjectRepo by inject()
    private val messagesRepo: MessagesRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _login = MutableValue("")
    private val _password = MutableValue("")
    private val _isPasswordHidden = MutableValue(true)
    private val _isProgressShown = MutableValue(false)

    fun onLoginChange(value: String) {
        _login.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onPasswordHiddenChange(value: Boolean) {
        _isPasswordHidden.value = value
    }

    fun onLogin() {
        if (isInputInvalid()) return

        _isProgressShown.value = true

        scope.launch(dispatchers.IO) {
            authorize()
            _isProgressShown.value = false
        }
    }

    private fun isInputInvalid(): Boolean =
        (!loginValidator.validate(_login.value) && !passwordValidator.validate(_password.value)).also {
            // if invalid - show on ui.
            if (it) {
                exceptionController.show(RuntimeException(Values.LOGIN_OR_PASS_IS_NOT_RIGHT))
            }
        }

    private suspend fun authorize() {
        if (profileRepo.login(_login.value, _password.value)) {
            scope.launch(dispatchers.IO) { subjectRepo.load() }
            scope.launch(dispatchers.IO) { messagesRepo.loadMessages() }
            mainNavigationController.navigateToProfile()
        }
    }
}