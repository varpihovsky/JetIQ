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
import com.arkivanov.decompose.childContext
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.feature_auth.AuthComponent
import com.varpihovsky.feature_messages.MessagesRootComponent
import com.varpihovsky.feature_profile.ProfileRootComponent
import com.varpihovsky.feature_settings.SettingsRootComponent
import com.varpihovsky.ui_root.appbar.AppbarComponent
import com.varpihovsky.ui_root.bottomBar.BottomBarComponent
import com.varpihovsky.ui_root.exceptions.ExceptionComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponent(componentContext: ComponentContext) : ComponentContext by componentContext, KoinComponent {
    val appbarComponent = AppbarComponent(childContext("AppbarComponent"))
    val bottomBarComponent = BottomBarComponent(childContext("BottomBarComponent"))
    val exceptionComponent = ExceptionComponent(childContext("ExceptionComponent"))
    val routerState by lazy { rootRouter.state }

    private val profileRepo: ProfileRepo by inject()

    private val rootRouter = RootRouter(
        componentContext = componentContext,
        initial = if (profileRepo.getProfileDTO() == null) {
            RootRouter.Config.Auth
        } else {
            RootRouter.Config.Profile
        },
        appBarController = appbarComponent,
        bottomBarController = bottomBarComponent,
        exceptionController = exceptionComponent,
    )

    sealed class RootChild {
        class Auth(val component: AuthComponent) : RootChild()
        class Profile(val component: ProfileRootComponent) : RootChild()
        class Messages(val component: MessagesRootComponent) : RootChild()
        class Settings(val component: SettingsRootComponent) : RootChild()
    }
}