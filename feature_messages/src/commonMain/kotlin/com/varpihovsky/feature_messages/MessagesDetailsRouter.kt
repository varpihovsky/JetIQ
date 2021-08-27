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

import com.arkivanov.decompose.popWhile
import com.arkivanov.decompose.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_messages.chat.ChatComponent
import com.varpihovsky.feature_messages.groupMessage.GroupMessageComponent
import com.varpihovsky.ui_data.dto.UIReceiverDTO

internal class MessagesDetailsRouter(
    jetIQComponentContext: JetIQComponentContext,
    navigationController: MessagesNavigationController,
) : JetIQComponentContext by jetIQComponentContext {
    val routerState by lazy { router.state }

    private val router = jetIQComponentContext.messagesRouter(
        initialConfiguration = { Config.None },
        configurationClass = Config::class,
        key = "MessagesDetailsRouter",
        navigationController = navigationController,
        childFactory = ::createChild,
    )

    private var currentContact: UIReceiverDTO? = null

    private fun createChild(
        config: Config,
        messagesComponentContext: MessagesComponentContext
    ): MessagesRootComponent.DetailsChild = when (config) {
        Config.None -> MessagesRootComponent.DetailsChild.None
        Config.Chat -> MessagesRootComponent.DetailsChild.Chat(
            ChatComponent(
                messagesComponentContext,
                checkNotNull(currentContact)
            )
        )
        Config.GroupMessage -> MessagesRootComponent.DetailsChild.GroupMessage(
            GroupMessageComponent(messagesComponentContext)
        )
    }

    fun navigateToChat(chat: UIReceiverDTO) {
        currentContact = chat
        router.popWhile { it !is Config.None }
        router.push(Config.Chat)
    }

    fun navigateToGroupMessage() {
        router.navigate { stack -> stack.dropLastWhile { it !is Config.None }.plus(Config.GroupMessage) }
    }

    fun hide() {
        router.popWhile { it !is Config.None }
    }

    fun isShown() = router.state.value.activeChild.configuration !is Config.None

    sealed class Config : Parcelable {
        @Parcelize
        object None : Config()

        @Parcelize
        object Chat : Config()

        @Parcelize
        object GroupMessage : Config()
    }
}