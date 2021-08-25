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

import com.arkivanov.decompose.Router
import com.arkivanov.essenty.parcelable.Parcelable
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_lifecycle.jetIQRouter
import kotlin.reflect.KClass

internal interface SettingsComponentContext : JetIQComponentContext {
    val settingsNavigationHandler: SettingsNavigationHandler
}

internal class DefaultSettingsComponentContext(
    jetIQComponentContext: JetIQComponentContext,
    override val settingsNavigationHandler: SettingsNavigationHandler
) : SettingsComponentContext, JetIQComponentContext by jetIQComponentContext

internal fun <C : Parcelable, T : Any> JetIQComponentContext.settingsRouter(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    settingsNavigationHandler: SettingsNavigationHandler,
    childFactory: (configuration: C, SettingsComponentContext) -> T
): Router<C, T> = jetIQRouter(
    initialConfiguration = initialConfiguration,
    initialBackStack = initialBackStack,
    configurationClass = configurationClass,
    key = key,
    handleBackButton = handleBackButton,
    bottomBarController = bottomBarController,
    appBarController = appBarController,
    exceptionController = exceptionController,
    mainNavigationController = mainNavigationController,
    drawerController = drawerController,
    childFactory = { configuration, jetIQComponentContext ->
        childFactory(
            configuration,
            DefaultSettingsComponentContext(jetIQComponentContext, settingsNavigationHandler)
        )
    }
)