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
package com.varpihovsky.ui_root.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.AppBarController

class AppbarComponent(componentContext: ComponentContext) : ComponentContext by componentContext, AppBarController {
    private val _text = MutableValue("")
    val text: Value<String> = _text

    private val _navigationIcon = MutableValue<NavigationIconState>(NavigationIconState.Empty)
    val navigationIcon: Value<NavigationIconState> = _navigationIcon

    private val _actions = MutableValue<@Composable RowScope.() -> Unit> {}
    val actions: Value<@Composable RowScope.() -> Unit> = _actions

    private val _isShown = MutableValue(false)
    val isShown: Value<Boolean> = _isShown

    override fun setText(text: String) {
        _text.value = text
    }

    override fun setIconToDrawer() {
        _navigationIcon.value = NavigationIconState.Drawer
    }

    override fun setIconToBack() {
        _navigationIcon.value = NavigationIconState.Back
    }

    override fun hideIcon() {
        _navigationIcon.value = NavigationIconState.Empty
    }

    override fun setActions(actions: RowScope.() -> Unit) {
        _actions.value = actions
    }

    override fun emptyActions() {
        _actions.value = {}
    }

    override fun show() {
        _isShown.value = true
    }

    override fun hide() {
        _isShown.value = false
    }
}