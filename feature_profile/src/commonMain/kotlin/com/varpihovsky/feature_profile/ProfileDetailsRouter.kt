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
package com.varpihovsky.feature_profile

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.consume
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_profile.subjects.markbook.MarkbookSubjectComponent
import com.varpihovsky.feature_profile.subjects.success.SuccessSubjectComponent

internal class ProfileDetailsRouter(
    jetIQComponentContext: JetIQComponentContext,
    navigationManager: ProfileNavigationManager
) : JetIQComponentContext by jetIQComponentContext {
    val routerState: Value<RouterState<Config, ProfileRootComponent.DetailsChild>> by lazy { router.state }

    private var id: Int = stateKeeper.consume<SavedState>(STATE_KEEPER_SUBJECT_ID_KEY)?.id ?: 0
    private var isInFullScreen: Boolean =
        stateKeeper.consume<SavedState>(STATE_KEEPER_SUBJECT_ID_KEY)?.isInFullScreen ?: false

    private val router = jetIQComponentContext.profileRouter(
        initialConfiguration = { Config.None },
        configurationClass = Config::class,
        key = "ProfileDetailsRouter",
        navigationManager = navigationManager,
        childFactory = ::createChild
    )

    init {
        stateKeeper.register(STATE_KEEPER_SUBJECT_ID_KEY) { SavedState(id, isInFullScreen) }
    }

    private fun createChild(
        config: Config,
        profileComponentContext: ProfileComponentContext
    ): ProfileRootComponent.DetailsChild = when (config) {
        Config.None -> ProfileRootComponent.DetailsChild.None
        Config.Success -> ProfileRootComponent.DetailsChild.Success(
            SuccessSubjectComponent(profileComponentContext, id, isInFullScreen)
        )
        Config.Markbook -> ProfileRootComponent.DetailsChild.Markbook(
            MarkbookSubjectComponent(profileComponentContext, id, isInFullScreen)
        )
    }

    fun setFullScreen(isInFullScreen: Boolean) {
        this.isInFullScreen = isInFullScreen

        when (val child = router.state.value.activeChild.instance) {
            is ProfileRootComponent.DetailsChild.Success -> child.component.setFullScreen(isInFullScreen)
            is ProfileRootComponent.DetailsChild.Markbook -> child.component.setFullScreen(isInFullScreen)
        }
    }

    fun navigateToSuccess(id: Int) {
        this.id = id
        router.navigate { stack -> stack.dropLastWhile { it is Config.None }.plus(Config.Success) }
    }

    fun navigateToMarkbook(id: Int) {
        this.id = id
        router.navigate { stack -> stack.dropLastWhile { it is Config.None }.plus(Config.Markbook) }
    }

    fun clear() {
        router.navigate { stack -> stack.dropLastWhile { it is Config.None } }
    }

    fun isShown() = router.state.value.activeChild.configuration != Config.None

    sealed class Config : Parcelable {
        @Parcelize
        object None : Config()

        @Parcelize
        object Success : Config()

        @Parcelize
        object Markbook : Config()
    }

    @Parcelize
    private class SavedState(val id: Int, val isInFullScreen: Boolean) : Parcelable

    companion object {
        const val STATE_KEEPER_SUBJECT_ID_KEY = "ProfileDetailsRouterSubjectId"
    }
}