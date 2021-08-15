package com.varpihovsky.core_db.dao

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

import com.varpihovsky.core_db.internal.*
import com.varpihovsky.core_db.internal.types.MessageInternal
import com.varpihovsky.core_db.internal.types.mappers.toExternal
import com.varpihovsky.core_db.internal.types.mappers.toInternal
import com.varpihovsky.jetiqApi.data.Message
import kotlinx.coroutines.flow.Flow
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

    companion object {
        operator fun invoke(db: DB): MessageDAO = MessageDAOImpl(db)
    }
}

private class MessageDAOImpl(private val db: DB) : MessageDAO {
    private val dataFetcher = DataFetcher<MessageInternal>(db.allList())

    init {
        db.on<MessageInternal>().register(dataFetcher)
    }

    override fun getMessages(): Flow<List<Message>> {
        return dataFetcher.flow.map { it.map { internal -> internal.toExternal() } }
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
}