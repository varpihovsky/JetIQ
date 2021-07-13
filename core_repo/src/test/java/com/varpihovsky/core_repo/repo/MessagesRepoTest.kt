package com.varpihovsky.core_repo.repo

import com.varpihovsky.core_db.dao.MessageDAO
import com.varpihovsky.core_network.managers.JetIQMessageManager
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.core_repo.testCore.ConfidentRepoTest
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.MessageToSendDTO
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

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
            profileDAO
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getMessages returns messages flow`() = runBlockingTest {
        every { messageDAO.getMessages() } returns flow { emit(TEST_MESSAGES) }
        assertEquals(TEST_MESSAGES, messagesRepo.getMessages().last())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test loadMessages adds messages to database`() = runBlockingTest {
        coEvery { jetIQMessageManager.getMessages(TEST_PROFILE.session!!) } returns Result.success(
            TEST_MESSAGES
        )
        messagesRepo.loadMessages()
        verify { messageDAO.addMessage(TEST_MESSAGES.first()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test sendMessage sends message`() = runBlockingTest {
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
    fun `Test refreshing toggles loading state`() = runBlockingTest {
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