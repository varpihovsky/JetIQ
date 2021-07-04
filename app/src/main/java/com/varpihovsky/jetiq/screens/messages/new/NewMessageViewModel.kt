package com.varpihovsky.jetiq.screens.messages.new

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.navigation.NavigationManager
import com.varpihovsky.jetiq.ui.appbar.AppbarManager
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewMessageViewModel @Inject constructor(
    appbarManager: AppbarManager,
    private val navigationManager: NavigationManager,
) : JetIQViewModel(appbarManager, navigationManager) {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)

    private val receivers = MutableLiveData<List<UIReceiverDTO>>()
    private val messageFieldValue = MutableLiveData("")

    inner class Data {
        val receivers: LiveData<List<UIReceiverDTO>> = this@NewMessageViewModel.receivers
        val messageFieldValue: LiveData<String> = this@NewMessageViewModel.messageFieldValue
    }

    fun onReceiverRemove(receiver: UIReceiverDTO) {

    }

    fun onNewReceiverButtonClick() {

    }

    fun onMessageValueChange(change: String) {

    }

    fun onSendClick() {

    }
}