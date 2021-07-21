package com.varpihovsky.jetiq.screens.messages.main

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

import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.jetiq.testCore.ViewModelTest
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class MessagesViewModelTest : ViewModelTest() {
    private lateinit var messagesViewModel: MessagesViewModel
    private val messagesModel: MessagesRepo = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() = runBlockingTest {
        super.setup()

        every { messagesModel.getMessages() } returns flow {
            emit(TEST_MESSAGES)
        }

        messagesViewModel = MessagesViewModel(
            viewModelDispatchers,
            navigationController,
            messagesModel,
            appbarManager,
            exceptionEventManager
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test messages are shown`() = runBlockingTest {
        assertEquals(TEST_MESSAGES.map { it.toUIDTO() }, messagesViewModel.data.messages.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test navigates to new messages screen on new message button click`() = runBlockingTest {
        messagesViewModel.onNewMessageButtonClick()
        verify(exactly = 1) { navigationController.manage(NavigationDirections.newMessage.destination) }
    }

    companion object {
        private val TEST_MESSAGES = listOf(
            MessageDTO(
                "<b>Мартинюк В.В.</b>:<br>сьогодні о 10.20 у вас буде можливість здати ЛР https://" +
                        "meet.google.com/cnn-njor-zr",
                "894",
                "1",
                "669926",
                "1624430641"
            ),
            MessageDTO(
                "<b>Мартинюк В.В.</b>:<br>Доброго ранку, через технічні проблеми прошу усіх з\"" +
                        "явитися на іспит на 11.00 посилання на іспит https://meet.google.com/cnn-njor-zre",
                "894",
                "1",
                "668526",
                "1624254011"
            )
        )
    }
}