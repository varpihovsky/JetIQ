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

import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.flow.map

class MessagesViewModel(
    private val navigationManager: NavigationController,
    private val messagesModel: MessagesRepo,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationManager, exceptionEventManager), Refreshable {
    override val isLoading
        get() = messagesModel.isLoading

    val messages = messagesModel.getMessages()
        .map { messages -> messages.sortedByDescending { it.time.toLong() }.map { it.toUIDTO() } }

    init {
        messagesModel.loadMessages()
    }

    fun onNewMessageButtonClick() {
        navigationManager.manage(NavigationDirections.newMessage.destination)
    }

    override fun onRefresh() {
        messagesModel.onRefresh()
    }

    fun onContactsClick() {
        navigationManager.manage(NavigationDirections.contacts.destination)
    }

}