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
package com.varpihovsky.feature_messages.wall

import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class MessagesComponent(
    messagesComponentContext: MessagesComponentContext
) : MessagesComponentContext by messagesComponentContext, KoinComponent {
    val isLoading get() = messagesRepo.isLoading
    val messages
        get() = messagesRepo.getMessages().map { messages ->
            messages.sortedByDescending { it.time.toLong() }.map { it.toUIDTO() }
        }

    private val messagesRepo: MessagesRepo by inject()
    private val dispatchers: CoroutineDispatchers by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    fun onNewMessageButtonClick() {
        navigationController.navigateToNewMessage()
    }

    fun onRefresh() {
        scope.launch(dispatchers.IO) {
            messagesRepo.onRefresh()
        }
    }

    fun onContactsClick() {
        navigationController.navigateToContacts()
    }
}