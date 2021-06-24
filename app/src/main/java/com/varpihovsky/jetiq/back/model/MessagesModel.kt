package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.managers.JetIQMessageManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.MessageDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.dto.MessageDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesModel @Inject constructor(
    private val jetIQMessageManager: JetIQMessageManager,
    private val messageDatabaseManager: MessageDatabaseManager,
    confidentialDatabaseManager: ConfidentialDatabaseManager,
    profileDatabaseManager: ProfileDatabaseManager
) : ConfidentModel(confidentialDatabaseManager, profileDatabaseManager) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun getAll(): Flow<List<MessageDTO>> {
        scope.launch { loadMessages() }
        return messageDatabaseManager.getAll()
    }

    private suspend fun loadMessages() {
        delay(50)
        jetIQMessageManager.getMessages(requireSession()).forEach { messageDTO ->
            messageDatabaseManager.putMessage(messageDTO)
        }
    }

}