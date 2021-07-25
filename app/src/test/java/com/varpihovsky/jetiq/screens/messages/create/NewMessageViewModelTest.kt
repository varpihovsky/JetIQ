package com.varpihovsky.jetiq.screens.messages.create

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

import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModelData
import com.varpihovsky.jetiq.testCore.ViewModelDataTransferTest
import com.varpihovsky.ui_data.ReceiverType
import com.varpihovsky.ui_data.UIReceiverDTO
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class NewMessageViewModelTest : ViewModelDataTransferTest() {
    private lateinit var viewModel: NewMessageViewModel
    private val messagesModel: MessagesRepo = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        initViewModel()
    }

    @ExperimentalCoroutinesApi
    private fun initViewModel() {
        viewModel = NewMessageViewModel(
            viewModelDispatchers,
            appbarManager,
            navigationController,
            viewModelDataTransferManager,
            messagesModel,
            exceptionEventManager
        )
        dataTransferStateFlow.value =
            ContactsViewModelData(TEST_RECEIVERS, ContactsViewModel::class)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Message value is changing`() {
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        assertEquals(MESSAGE_VALUE, viewModel.data.messageFieldValue.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Receivers are collected`() = runBlockingTest {
        assertEquals(TEST_RECEIVERS, viewModel.data.receivers.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Receivers are removing`() = runBlockingTest {
        viewModel.onReceiverRemove(TEST_RECEIVERS.first())
        assertEquals(listOf(TEST_RECEIVERS.last()), viewModel.data.receivers.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `On new receiver button click current receivers are passed`() = runBlockingTest {
        viewModel.onNewReceiverButtonClick()
        verify(exactly = 2) { dataTransferStateFlow setProperty "value" value any() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `On new receiver button click navigates to contacts`() = runBlockingTest {
        viewModel.onNewReceiverButtonClick()
        verify(exactly = 1) { navigationController.manage(NavigationDirections.contacts.destination) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty message cant to be sent`() = runBlockingTest {
        viewModel.onSendClick()
        coVerify(exactly = 0) { messagesModel.sendMessage(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty receivers message cant to be sent`() = runBlockingTest {
        TEST_RECEIVERS.forEach { viewModel.onReceiverRemove(it) }
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        viewModel.onSendClick()
        coVerify(exactly = 0) { messagesModel.sendMessage(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty message exception is shown`() = runBlockingTest {
        viewModel.onSendClick()
        verify { exceptionEventManager.pushException(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty receivers exception is shown`() = runBlockingTest {
        TEST_RECEIVERS.forEach { viewModel.onReceiverRemove(it) }
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        viewModel.onSendClick()
        verify { exceptionEventManager.pushException(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Message is sent to all receivers when everything is alright`() = runBlockingTest {
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        viewModel.onSendClick()
        coVerify(exactly = 2) { messagesModel.sendMessage(any()) }
    }

    companion object {
        val TEST_RECEIVERS = listOf(
            UIReceiverDTO(0, "Sample Receiver 1", ReceiverType.TEACHER),
            UIReceiverDTO(1, "Sample Receiver 2", ReceiverType.STUDENT)
        )

        const val MESSAGE_VALUE = "Some message"
    }
}