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
package com.varpihovsky.feature_messages

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.consume
import com.varpihovsky.core.util.JetIQPlatformTools
import com.varpihovsky.core.util.Platform
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_messages.contacts.ContactsComponent
import com.varpihovsky.feature_messages.wall.MessagesComponent
import com.varpihovsky.ui_data.dto.UIReceiverDTO

internal class MessagesMainRouter(
    jetIQComponentContext: JetIQComponentContext,
    navigationController: MessagesNavigationController,
    private val onChatSelected: (UIReceiverDTO) -> Unit
) : JetIQComponentContext by jetIQComponentContext {
    val routerState: Value<RouterState<Config, MessagesRootComponent.MainChild>> by lazy { router.state }

    private val router = jetIQComponentContext.messagesRouter(
        configurationClass = Config::class,
        key = "MessagesMainRouter",
        childFactory = ::createChild,
        navigationController = navigationController,
        initialConfiguration = {
            when (JetIQPlatformTools.currentPlatform) {
                Platform.Android -> Config.Wall
                Platform.JVM -> Config.Contacts
            }
        }
    )

    private var isSquashed: Boolean = stateKeeper.consume<SavedState>(STATE_KEEPER_KEY)?.isSquashed ?: true
    private var lastStack: List<Config> = stateKeeper.consume<SavedState>(STATE_KEEPER_KEY)?.lastStack ?: listOf()

    init {
        stateKeeper.register(STATE_KEEPER_KEY) { SavedState(isSquashed, lastStack) }
    }

    private fun createChild(
        config: Config,
        messagesComponentContext: MessagesComponentContext
    ): MessagesRootComponent.MainChild = when (config) {
        Config.None -> MessagesRootComponent.MainChild.None
        Config.Wall -> MessagesRootComponent.MainChild.Wall(MessagesComponent(messagesComponentContext))
        Config.Contacts -> MessagesRootComponent.MainChild.Contacts(
            ContactsComponent(
                messagesComponentContext,
                isExternalChoosing = false,
                isUnknownContactOn = !isSquashed,
                onContactClick = ::onChatSelected.get()
            )
        )
    }

    fun show() {
        router.navigate { lastStack.ifEmpty { listOf(Config.Wall) } }
    }

    fun navigateToContacts() {
        router.navigate { stack ->
            if (stack.lastOrNull() is Config.Contacts) {
                return@navigate stack
            }

            stack.plus(Config.Contacts)
        }
    }

    fun navigateToWall() {
        router.navigate { listOf(Config.Wall) }
    }

    fun setSquashed(isSquashed: Boolean) {
        this.isSquashed = isSquashed

        if (isSquashed && isShown()) {
            navigateToWall()
        }
        if (!isSquashed) {
            navigateToContacts()
        }
    }

    fun hide() {
        saveStack()
        router.navigate { listOf(Config.None) }
    }

    private fun isShown() = router.state.value.activeChild.configuration !is Config.None

    private fun saveStack() {
        lastStack = router.state.value.backStack.map { it.configuration } + routerState.value.activeChild.configuration
    }

    sealed class Config : Parcelable {
        @Parcelize
        object None : Config()

        @Parcelize
        object Wall : Config()

        @Parcelize
        object Contacts : Config()
    }

    @Parcelize
    private class SavedState(val isSquashed: Boolean, val lastStack: List<Config>) : Parcelable

    companion object {
        const val STATE_KEEPER_KEY = "MessageMainRouterState"
    }
}