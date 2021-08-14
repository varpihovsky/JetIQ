package com.varpihovsky.core_repo.repo

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

import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_repo.testCore.ConfidentRepoTest
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MessagesRepoTest : ConfidentRepoTest() {
    private lateinit var messagesRepo: MessagesRepo

    private val jetIQMessageManager: JetIQMessageManager = mockk(relaxed = true)
    private val messageDAO: MessageDAO = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        messagesRepo = MessagesRepo(
            jetIQMessageManager,
            messageDAO,
            confidentialDAO,
            profileDAO,
            exceptionEventManager
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getMessages_returns_messages_flow() = runBlockingTest {
        every { messageDAO.getMessages() } returns flow { emit(TEST_MESSAGES) }
        assertEquals(TEST_MESSAGES, messagesRepo.getMessages().last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_loadMessages_adds_messages_to_database() = runBlockingTest {
        coEvery { jetIQMessageManager.getMessages(TEST_PROFILE.session!!) } returns Result.success(
            TEST_MESSAGES
        )
        messagesRepo.loadMessages()
        verify { messageDAO.addMessage(TEST_MESSAGES.first()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_sendMessage_sends_message() = runBlockingTest {
        coEvery { jetIQMessageManager.getCsrf(TEST_PROFILE.session!!) } returns Result.success(
            TEST_CSRF
        )
        messagesRepo.sendMessage(TEST_MESSAGE_TO_SEND)
        coVerify {
            jetIQMessageManager.sendMessage(
                TEST_PROFILE.session!!,
                TEST_CSRF,
                TEST_MESSAGE_TO_SEND
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_refreshing_toggles_loading_state() = runBlockingTest {
        coEvery { jetIQMessageManager.getMessages(TEST_PROFILE.session!!) } answers object :
            Answer<Result<List<MessageDTO>>> {
            override fun answer(call: Call): Result<List<MessageDTO>> {
                assertTrue(messagesRepo.isLoading.value)
                return Result.success(TEST_MESSAGES)
            }

        }
        messagesRepo.onRefresh()
    }

    companion object {
        val TEST_MESSAGES = listOf(MessageDTO("Hello, world!", "1", "1", "1", "1"))
        val TEST_CSRF = CSRF("example")
        val TEST_MESSAGE_TO_SEND = MessageToSendDTO(1, 1, "Hello, World!")
    }
}