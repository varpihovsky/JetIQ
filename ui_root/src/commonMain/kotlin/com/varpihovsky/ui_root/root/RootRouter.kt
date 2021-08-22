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
package com.varpihovsky.ui_root.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.varpihovsky.core_lifecycle.*
import com.varpihovsky.feature_auth.AuthComponent
import com.varpihovsky.feature_messages.MessagesRootComponent
import com.varpihovsky.feature_profile.ProfileRootComponent
import com.varpihovsky.feature_settings.SettingsRootComponent

class RootRouter(
    componentContext: ComponentContext,
    bottomBarController: BottomBarController,
    appBarController: AppBarController,
    exceptionController: ExceptionController,
    private val initial: Config
) : MainNavigationController {
    val state by lazy { router.state }

    private val router = componentContext.jetIQRouter(
        initialConfiguration = { initial },
        key = "RootRouter",
        childFactory = ::createChild,
        handleBackButton = true,
        bottomBarController = bottomBarController,
        appBarController = appBarController,
        configurationClass = Config::class,
        exceptionController = exceptionController,
        mainNavigationController = this
    )

    private fun createChild(config: Config, componentContext: JetIQComponentContext) =
        when (config) {
            Config.Auth -> RootComponent.RootChild.Auth(AuthComponent(componentContext))
            Config.Profile -> RootComponent.RootChild.Profile(ProfileRootComponent(componentContext))
            Config.Messages -> RootComponent.RootChild.Messages(MessagesRootComponent(componentContext))
            Config.Settings -> RootComponent.RootChild.Settings(SettingsRootComponent(componentContext))
        }

    override fun navigateToProfile() {
        router.navigate { listOf(Config.Profile) }
    }

    override fun navigateToSettings() {
        router.push(Config.Settings)
    }

    override fun navigateToAuth() {
        router.navigate { listOf(Config.Auth) }
    }

    sealed class Config : Parcelable {
        @Parcelize
        object Auth : Config()

        @Parcelize
        object Profile : Config()

        @Parcelize
        object Messages : Config()

        @Parcelize
        object Settings : Config()
    }
}