package com.varpihovsky.jetiq.screens.messages.main

import com.varpihovsky.jetiq.back.dto.MessageDTO
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import com.varpihovsky.jetiq.testCore.ViewModelTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class MessagesViewModelTest : ViewModelTest() {
    private lateinit var messagesViewModel: MessagesViewModel
    private val messagesModel: MessagesModel = mockk(relaxed = true)
    private val connectionManager: ConnectionManager = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    override fun setup() = runBlockingTest {
        super.setup()

        every { messagesModel.getMessagesState() } returns flow {
            emit(TEST_MESSAGES)
        }

        messagesViewModel = MessagesViewModel(
            viewModelDispatchers,
            navigationManager,
            messagesModel,
            connectionManager,
            appbarManager
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
        verify(exactly = 1) { navigationManager.manage(NavigationDirections.newMessage) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When internet available refreshing is called`() = runBlockingTest {
        every { connectionManager.isConnected(any()) } returns true
        messagesViewModel.onRefresh()
        verify(exactly = 1) { messagesModel.onRefresh() }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When internet unavailable shows exception`() = runBlockingTest {
        every { connectionManager.isConnected(any()) } returns false
        messagesViewModel.onRefresh()
        assertTrue(messagesViewModel.exceptions.firstOrNull() != null)
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