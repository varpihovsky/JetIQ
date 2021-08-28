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
import com.arkivanov.decompose.popWhile
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_settings.about.AboutSettingsComponent
import com.varpihovsky.feature_settings.general.GeneralSettingsComponent

internal class SettingsDetailsRouter(
    jetIQComponentContext: JetIQComponentContext,
    settingsNavigationHandler: SettingsNavigationHandler
) {
    val state: Value<RouterState<Config, SettingsRootComponent.DetailsChild>> by lazy { router.state }

    private val router = jetIQComponentContext.settingsRouter(
        initialConfiguration = { Config.None },
        key = "SettingsDetailsRootRouter",
        configurationClass = Config::class,
        childFactory = ::createChild,
        settingsNavigationHandler = settingsNavigationHandler
    )

    private fun createChild(
        config: Config,
        componentContext: SettingsComponentContext
    ): SettingsRootComponent.DetailsChild = when (config) {
        Config.None -> SettingsRootComponent.DetailsChild.None
        Config.GeneralSettings -> SettingsRootComponent.DetailsChild.General(GeneralSettingsComponent(componentContext))
        Config.AboutSettings -> SettingsRootComponent.DetailsChild.About(AboutSettingsComponent(componentContext))
    }

    fun navigateToGeneralSettings() {
        router.navigate { stack ->
            if (stack.lastOrNull() is Config.GeneralSettings) return@navigate stack
            stack.dropLastWhile { it !is Config.None }.plus(Config.GeneralSettings)
        }
    }

    fun navigateToAboutSettings() {
        router.navigate { stack ->
            if (stack.lastOrNull() is Config.AboutSettings) return@navigate stack
            stack.dropLastWhile { it !is Config.None }.plus(Config.AboutSettings)
        }
    }

    fun hide() {
        router.popWhile { it !is Config.None }
    }

    fun isShown() = router.state.value.activeChild.configuration !is Config.None

    sealed class Config : Parcelable {
        @Parcelize
        object None : Config()

        @Parcelize
        object GeneralSettings : Config()

        @Parcelize
        object AboutSettings : Config()
    }
}