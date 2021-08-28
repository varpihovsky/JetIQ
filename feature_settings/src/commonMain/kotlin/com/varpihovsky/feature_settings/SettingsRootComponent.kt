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
package com.varpihovsky.feature_settings

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.log.d
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_settings.about.AboutSettingsComponent
import com.varpihovsky.feature_settings.general.GeneralSettingsComponent
import com.varpihovsky.feature_settings.main.MainSettingsComponent

class SettingsRootComponent(
    jetIQComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetIQComponentContext, SettingsNavigationHandler {
    internal val detailsRouterState: Value<RouterState<SettingsDetailsRouter.Config, DetailsChild>> by lazy { detailsRouter.state }
    internal val mainRouterState: Value<RouterState<SettingsMainRouter.Config, MainChild>> by lazy { mainRouter.routerState }
    internal val isMultiPane: Value<Boolean> by lazy { _isMultiPane }

    private val detailsRouter = SettingsDetailsRouter(jetIQComponentContext, this)
    private val mainRouter = SettingsMainRouter(jetIQComponentContext, this)
    private val _isMultiPane = MutableValue(false)

    init {
        backPressedDispatcher.register {
            if (_isMultiPane.value || !detailsRouter.isShown()) {
                return@register false
            }
            detailsRouter.hide()
            mainRouter.show()
            true
        }
    }

    override fun navigateToGeneral() {
        detailsRouter.navigateToGeneralSettings()
        processMultiPane()
    }

    override fun navigateToAbout() {
        detailsRouter.navigateToAboutSettings()
        processMultiPane()
    }

    override fun navigateToAuth() {
        mainNavigationController.navigateToAuth()
    }

    fun setMultiPane(isMultiPane: Boolean) {
        _isMultiPane.value = isMultiPane

        if (isMultiPane) {
            switchToMultiPane()
        } else {
            switchToSinglePane()
        }
    }

    private fun processMultiPane() {
        if (_isMultiPane.value) {
            mainRouter.show()
        } else {
            mainRouter.hide()
        }
    }

    private fun switchToMultiPane() {
        mainRouter.show()
    }

    private fun switchToSinglePane() {
        d(detailsRouter.isShown().toString())
        if (detailsRouter.isShown()) {
            mainRouter.hide()
        }
    }

    internal sealed class DetailsChild {
        object None : DetailsChild()
        class General(val component: GeneralSettingsComponent) : DetailsChild()
        class About(val component: AboutSettingsComponent) : DetailsChild()
    }

    internal sealed class MainChild {
        object None : MainChild()
        class Main(val component: MainSettingsComponent) : MainChild()
    }
}