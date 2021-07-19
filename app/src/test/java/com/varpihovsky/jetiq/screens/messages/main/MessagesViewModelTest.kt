package com.varpihovsky.jetiq.screens.messages.main

import com.varpihovsky.core.ConnectionManager
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
    private val connectionManager: ConnectionManager = mockk(relaxed = true)

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
            connectionManager,
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
        verify { exceptionEventManager.pushException(any()) }
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