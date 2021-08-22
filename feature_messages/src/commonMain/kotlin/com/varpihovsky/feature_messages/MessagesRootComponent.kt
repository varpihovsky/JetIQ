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

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.feature_messages.chat.ChatComponent
import com.varpihovsky.feature_messages.contacts.ContactsComponent
import com.varpihovsky.feature_messages.messaging.GroupMessageComponent
import com.varpihovsky.feature_messages.wall.MessagesComponent
import com.varpihovsky.ui_data.dto.UIReceiverDTO

class MessagesRootComponent(
    jetIQComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetIQComponentContext, MessagesNavigationController {
    internal val mainRouterState by lazy { mainRouter.routerState }
    internal val detailsRouterState by lazy { detailsRouter.routerState }
    internal val isMultiPane: Value<Boolean> by lazy { _isMultiPane }

    private val mainRouter = MessagesMainRouter(jetIQComponentContext, this, ::onChatSelected)
    private val detailsRouter = MessagesDetailsRouter(jetIQComponentContext, this)

    private val _isMultiPane = MutableValue(false)

    override fun navigateToContacts() {
        mainRouter.navigateToContacts()
    }

    override fun navigateToNewMessage() {

    }

    private fun onChatSelected(chat: UIReceiverDTO) {
        detailsRouter.navigateToChat(chat)
    }

    internal sealed class MainChild {
        object None : MainChild()
        class Wall(val component: MessagesComponent) : MainChild()
        class Contacts(val component: ContactsComponent) : MainChild()
    }

    internal fun setMultiPane(isMultiPane: Boolean) {
        _isMultiPane.value = isMultiPane

        if (isMultiPane) {
            switchToMultiPane()
        } else {
            switchToSinglePane()
        }
    }

    private fun switchToSinglePane() {
        if (detailsRouter.isShown()) {
            mainRouter.hide()
        }
        mainRouter.setSquashed(true)
    }

    private fun switchToMultiPane() {
        mainRouter.setSquashed(false)
    }

    internal sealed class DetailsChild {
        object None : DetailsChild()
        class Chat(val component: ChatComponent) : DetailsChild()
        class GroupMessage(val component: GroupMessageComponent) : DetailsChild()

        // Should only be used when selecting contacts for group message in SinglePane mode.
        class Contacts(val component: ContactsComponent) : DetailsChild()
    }
}