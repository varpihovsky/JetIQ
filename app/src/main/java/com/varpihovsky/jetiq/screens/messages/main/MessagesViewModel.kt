package com.varpihovsky.jetiq.screens.messages.main

import androidx.compose.runtime.State
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core.util.ReactiveTask
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.ui_data.UIMessageDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

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

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val navigationManager: NavigationController,
    private val messagesModel: MessagesRepo,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
) : JetIQViewModel(appbarManager, navigationManager, exceptionEventManager), Refreshable {
    val data by lazy { Data() }
    override val isLoading
        get() = messagesModel.isLoading

    private val messages = mutableStateOf(listOf<UIMessageDTO>())
    private val messagesTask =
        ReactiveTask(task = this::collectMessages, dispatcher = dispatchers.IO)

    inner class Data {
        val messages: State<List<UIMessageDTO>> = this@MessagesViewModel.messages
    }

    init {
        messagesTask.start()
        messagesModel.loadMessages()
    }

    private suspend fun collectMessages() {
        messagesModel.getMessages().collect { DTOMessages ->
            DTOMessages.map { it.toUIDTO() }.also { messages.value = it }
        }
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