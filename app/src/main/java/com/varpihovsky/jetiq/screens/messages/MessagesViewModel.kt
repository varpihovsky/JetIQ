package com.varpihovsky.jetiq.screens.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varpihovsky.jetiq.back.model.MessagesModel
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.system.util.ReactiveTask
import com.varpihovsky.jetiq.ui.dto.UIMessageDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
    private val messagesModel: MessagesModel
) : ViewModel() {
    val data by lazy { Data() }

    private val messages = MutableLiveData<List<UIMessageDTO>>()
    private val messagesTask = ReactiveTask(task = this::collectMessages)

    inner class Data {
        val messages: LiveData<List<UIMessageDTO>> = this@MessagesViewModel.messages
    }

    init {
        messagesTask.start()
    }

    private suspend fun collectMessages() {
        messagesModel.getAll().collect { DTOMessages ->
            DTOMessages.map {
                val split = it.body.split("<b>", "</b>:<br>")
                UIMessageDTO(
                    it.msg_id.toInt(),
                    split[1],
                    split[2],
                    Date(it.time.toLong()).toString()
                )
            }.also {
                messages.postValue(it)
            }
        }
    }

    fun onNewMessageButtonClick() {

    }

}