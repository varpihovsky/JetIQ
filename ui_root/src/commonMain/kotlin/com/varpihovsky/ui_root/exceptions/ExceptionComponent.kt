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
package com.varpihovsky.ui_root.exceptions

import com.arkivanov.decompose.ComponentContext
import com.varpihovsky.core_lifecycle.ExceptionController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExceptionComponent(componentContext: ComponentContext) : ComponentContext by componentContext,
    ExceptionController {
    // We use StateFlow here because Value needs T : Any, while Throwable doesn't extend it.
    val currentException: StateFlow<Throwable?> by lazy { _currentException }
    private val _currentException = MutableStateFlow<Throwable?>(null)

    override fun show(throwable: Throwable) {
        _currentException.value = throwable
    }
}