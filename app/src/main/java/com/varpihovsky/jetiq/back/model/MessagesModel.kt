package com.varpihovsky.jetiq.back.model

import androidx.lifecycle.MutableLiveData
import com.varpihovsky.jetiq.back.api.managers.JetIQMessageManager
import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.MessageDatabaseManager
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.system.exceptions.ModelExceptionSender
import com.varpihovsky.jetiq.system.exceptions.ViewModelExceptionReceivable
import com.varpihovsky.jetiq.system.util.logException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesModel @Inject constructor(
    private val jetIQMessageManager: JetIQMessageManager,
    private val messageDatabaseManager: MessageDatabaseManager,
    confidentialDatabaseManager: ConfidentialDatabaseManager,
    profileDatabaseManager: ProfileDatabaseManager
) : ConfidentModel(confidentialDatabaseManager, profileDatabaseManager), ModelExceptionSender,
    LoadableModel {
    override var receivable: ViewModelExceptionReceivable? = null
    override var isLoading = MutableLiveData(false)

    fun loadMessages() {
        isLoading.value = true
        modelScope.launch { processMessagesLoading() }
    }

    fun getMessagesState() = messageDatabaseManager.getAll()

    private suspend fun processMessagesLoading() {
        try {
            jetIQMessageManager.getMessages(requireSession()).forEach { messageDTO ->
                if (messageDTO.body != null) {
                    delay(100)
                    messageDatabaseManager.putMessage(messageDTO)
                }
            }
        } catch (e: RuntimeException) {
            receivable?.send(e)
            logException(e, getDebugPrefix(this) ?: "Application")
        }
        isLoading.postValue(false)
    }

    fun clearData() {
        messageDatabaseManager.removeAll()
    }
}