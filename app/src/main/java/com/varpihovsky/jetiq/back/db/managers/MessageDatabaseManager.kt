package com.varpihovsky.jetiq.back.db.managers

import com.varpihovsky.jetiq.back.db.dao.MessageDAO
import com.varpihovsky.jetiq.back.dto.MessageDTO
import javax.inject.Inject

class MessageDatabaseManager @Inject constructor(
    private val messageDAO: MessageDAO
) {
    fun putMessage(messageDTO: MessageDTO) {
        messageDAO.addMessage(messageDTO)
    }

    fun getAll() = messageDAO.getMessages()

    fun getMessageById(id: Int) = messageDAO.getMessageById(id)
}