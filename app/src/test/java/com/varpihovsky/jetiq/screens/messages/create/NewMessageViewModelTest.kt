package com.varpihovsky.jetiq.screens.messages.create

import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModel
import com.varpihovsky.jetiq.screens.messages.contacts.ContactsViewModelData
import com.varpihovsky.jetiq.system.exceptions.Values
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.testCore.ViewModelDataTransferTest
import com.varpihovsky.jetiq.ui.dto.ReceiverType
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class NewMessageViewModelTest : ViewModelDataTransferTest() {
    private lateinit var viewModel: NewMessageViewModel
    private val messagesModel: MessagesModel = mockk(relaxed = true)

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
            navigationManager,
            viewModelDataTransferManager,
            messagesModel
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
        verify(exactly = 1) { navigationManager.manage(NavigationDirections.contacts) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty message cant to be sent`() = runBlockingTest {
        viewModel.onSendClick()
        verify(exactly = 0) { messagesModel.sendMessage(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty receivers message cant to be sent`() = runBlockingTest {
        TEST_RECEIVERS.forEach { viewModel.onReceiverRemove(it) }
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        viewModel.onSendClick()
        verify(exactly = 0) { messagesModel.sendMessage(any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty message exception is shown`() = runBlockingTest {
        viewModel.onSendClick()
        assertEquals(
            RuntimeException(Values.EMPTY_MESSAGE).message,
            viewModel.exceptions.value?.message
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Empty receivers exception is shown`() = runBlockingTest {
        TEST_RECEIVERS.forEach { viewModel.onReceiverRemove(it) }
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        viewModel.onSendClick()
        assertEquals(
            RuntimeException(Values.EMPTY_RECEIVERS).message,
            viewModel.exceptions.value?.message
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Message is sent to all receivers when everything is alright`() = runBlockingTest {
        viewModel.onMessageValueChange(MESSAGE_VALUE)
        viewModel.onSendClick()
        verify(exactly = 2) { messagesModel.sendMessage(any()) }
    }

    companion object {
        val TEST_RECEIVERS = listOf(
            UIReceiverDTO(0, "Sample Receiver 1", ReceiverType.TEACHER),
            UIReceiverDTO(1, "Sample Receiver 2", ReceiverType.STUDENT)
        )

        const val MESSAGE_VALUE = "Some message"
    }
}