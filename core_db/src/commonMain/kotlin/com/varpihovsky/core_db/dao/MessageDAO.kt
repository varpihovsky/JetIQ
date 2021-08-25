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
package com.varpihovsky.core_db.dao

import com.varpihovsky.core_db.internal.*
import com.varpihovsky.core_db.internal.types.MessageInternal
import com.varpihovsky.core_db.internal.types.SentMessageInternal
import com.varpihovsky.core_db.internal.types.mappers.toExternal
import com.varpihovsky.core_db.internal.types.mappers.toInternal
import com.varpihovsky.jetiqApi.data.Message
import com.varpihovsky.repo_data.MessageToSendDTO
import com.varpihovsky.repo_data.ReadMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.kodein.db.DB
import org.kodein.db.flowOf
import org.kodein.db.keyById
import org.kodein.db.on

interface MessageDAO {
    fun getMessages(): Flow<List<Message>>

    fun getMessageById(id: Int): Flow<Message>

    fun addMessage(messageDTO: Message)

    fun deleteMessage(messageDTO: Message)

    fun deleteAll()

    fun isMessageRead(id: Int): Flow<Boolean>

    fun isMessageWasRead(id: Int): Boolean

    fun setMessageRead(id: Int)

    fun addSentMessage(messageToSendDTO: MessageToSendDTO)

    fun getSentMessages(receiverId: Int, receiverType: Int): Flow<List<MessageToSendDTO>>

    companion object {
        operator fun invoke(db: DB): MessageDAO = MessageDAOImpl(db)
    }
}

private class MessageDAOImpl(private val db: DB) : MessageDAO {
    private val messageDataFetcher = DataFetcher<MessageInternal>(db.allList())
    private val sentMessagesDataFetcher = DataFetcher<SentMessageInternal>(db.allList())

    init {
        db.on<MessageInternal>().register(messageDataFetcher)
    }

    override fun getMessages(): Flow<List<Message>> {
        return messageDataFetcher.flow.map { it.map { internal -> internal.toExternal() } }
    }

    override fun getMessageById(id: Int): Flow<Message> {
        return db.flowOf(db.keyById<MessageInternal>(id)).mapNotNull { it?.toExternal() }
    }

    override fun addMessage(messageDTO: Message) {
        db.putListable(model = messageDTO.toInternal())
    }

    override fun deleteMessage(messageDTO: Message) {
        db.delete(messageDTO.toInternal())
    }

    override fun deleteAll() {
        db.deleteAll<MessageInternal>()
    }

    override fun isMessageRead(id: Int): Flow<Boolean> {
        return db.flowOf(db.keyById<ReadMessage>()).filter { it?.id == id }.map { it != null }
    }

    override fun isMessageWasRead(id: Int): Boolean {
        return db.get<ReadMessage>(db.keyById(id)) != null
    }

    override fun setMessageRead(id: Int) {
        db.putListable(ReadMessage(id))
    }

    override fun addSentMessage(messageToSendDTO: MessageToSendDTO) {
        val id = db.allList<SentMessageInternal>().maxByOrNull { it.id }?.id ?: 0
        db.putListable(messageToSendDTO.toInternal(id))
    }

    override fun getSentMessages(receiverId: Int, receiverType: Int): Flow<List<MessageToSendDTO>> {
        return sentMessagesDataFetcher.flow.map { messages ->
            messages.map { it.toExternal() }.filter { it.id == receiverId && it.type == receiverType }
        }
    }
}