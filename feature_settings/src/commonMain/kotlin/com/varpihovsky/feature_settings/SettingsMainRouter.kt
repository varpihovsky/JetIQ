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
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_settings.main.MainSettingsComponent

internal class SettingsMainRouter(
    jetIQComponentContext: JetIQComponentContext,
    settingsNavigationHandler: SettingsNavigationHandler
) {
    val routerState: Value<RouterState<Config, SettingsRootComponent.MainChild>> by lazy { router.state }

    private val router = jetIQComponentContext.settingsRouter(
        initialConfiguration = { Config.Main },
        configurationClass = Config::class,
        key = "SettingsMainRouter",
        childFactory = ::createChild,
        settingsNavigationHandler = settingsNavigationHandler
    )

    private fun createChild(
        config: Config,
        settingsComponentContext: SettingsComponentContext
    ): SettingsRootComponent.MainChild = when (config) {
        Config.None -> SettingsRootComponent.MainChild.None
        Config.Main -> SettingsRootComponent.MainChild.Main(MainSettingsComponent(settingsComponentContext))
    }

    fun moveToBackStack() {
        if (router.state.value.activeChild.configuration !is Config.None) {
            router.push(Config.None)
        }
    }

    fun show() {
        if (router.state.value.activeChild.configuration !is Config.Main) {
            router.pop()
        }
    }

    sealed class Config : Parcelable {
        @Parcelize
        object None : Config()

        @Parcelize
        object Main : Config()
    }
}