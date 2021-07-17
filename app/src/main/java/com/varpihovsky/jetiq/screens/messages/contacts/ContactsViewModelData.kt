package com.varpihovsky.jetiq.screens.messages.contacts

import com.varpihovsky.core.dataTransfer.ViewModelData
import com.varpihovsky.ui_data.UIReceiverDTO
import kotlin.reflect.KClass

class ContactsViewModelData(data: List<UIReceiverDTO>, val sender: KClass<*>) :
    ViewModelData<List<UIReceiverDTO>>(data)