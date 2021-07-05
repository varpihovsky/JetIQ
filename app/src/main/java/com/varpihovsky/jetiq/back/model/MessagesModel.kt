package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.managers.JetIQMessageManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.MessageDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.back.dto.MessageDTO
import com.varpihovsky.jetiq.system.exceptions.ModelExceptionSender
import com.varpihovsky.jetiq.system.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.jetiq.system.util.logException
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
) : ConfidentModel(confidentialDatabaseManager, profileDatabaseManager), ModelExceptionSender {
    override var receivable: ViewModelExceptionReceivable? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    fun getAll(): Flow<List<MessageDTO>> {
        scope.launch { loadMessages() }
        return messageDatabaseManager.getAll()
    }

    private suspend fun loadMessages() {
        delay(50)
        try {
            jetIQMessageManager.getMessages(requireSession()).forEach { messageDTO ->
                messageDatabaseManager.putMessage(messageDTO)
            }
        } catch (e: RuntimeException) {
            receivable?.send(e)
            logException(e)
        }
    }
}