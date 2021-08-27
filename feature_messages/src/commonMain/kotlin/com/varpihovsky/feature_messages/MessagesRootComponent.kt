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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_lifecycle.childContext
import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.feature_messages.chat.ChatComponent
import com.varpihovsky.feature_messages.contacts.ContactsComponent
import com.varpihovsky.feature_messages.contacts.addition.ContactAdditionComponent
import com.varpihovsky.feature_messages.groupMessage.GroupMessageComponent
import com.varpihovsky.feature_messages.wall.MessagesComponent
import com.varpihovsky.repo_data.ContactDTO
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MessagesRootComponent(
    jetIQComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetIQComponentContext, MessagesNavigationController, KoinComponent {
    internal val contactAdditionComponent: State<ContactAdditionComponent?> by lazy { _contactAdditionComponent }
    internal val mainRouterState by lazy { mainRouter.routerState }
    internal val detailsRouterState by lazy { detailsRouter.routerState }
    internal val isMultiPane: Value<Boolean> by lazy { _isMultiPane }

    private val listRepo: ListRepo by inject()
    private val dispatchers: CoroutineDispatchers by inject()

    private val mainRouter = MessagesMainRouter(jetIQComponentContext, this, ::onChatSelected)
    private val detailsRouter = MessagesDetailsRouter(jetIQComponentContext, this)
    private val _isMultiPane = MutableValue(false)
    private val _contactAdditionComponent = mutableStateOf<ContactAdditionComponent?>(null)
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun navigateToContacts() {
        mainRouter.navigateToContacts()
    }

    override fun navigateToGroupMessage() {
        detailsRouter.navigateToGroupMessage()

        if (!_isMultiPane.value) {
            mainRouter.hide()
        }
    }

    private fun onChatSelected(chat: UIReceiverDTO) {
        detailsRouter.navigateToChat(chat)

        if (!_isMultiPane.value) {
            mainRouter.hide()
        }
    }

    internal fun newContactDialog() {
        _contactAdditionComponent.value = ContactAdditionComponent(childContext("ContactAdditionComponent"))
    }

    internal fun onConfirmButtonClick(contacts: List<UIReceiverDTO>) {
        scope.launch(dispatchers.IO) {
            contacts.map { ContactDTO(it.id, it.text, it.type.name.lowercase()) }
                .forEach { listRepo.addContact(it) }
        }
        onDismissRequest()
    }

    internal fun onDismissRequest() {
        _contactAdditionComponent.value = null
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

    internal sealed class MainChild {
        object None : MainChild()
        class Wall(val component: MessagesComponent) : MainChild()
        class Contacts(val component: ContactsComponent) : MainChild()
    }

    internal sealed class DetailsChild {
        object None : DetailsChild()
        class Chat(val component: ChatComponent) : DetailsChild()
        class GroupMessage(val component: GroupMessageComponent) : DetailsChild()
    }
}