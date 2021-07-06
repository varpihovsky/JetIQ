package com.varpihovsky.jetiq.screens.messages.contacts

import com.varpihovsky.jetiq.system.JetIQViewModel
import com.varpihovsky.jetiq.system.data_transfer.ViewModelData
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import kotlin.reflect.KClass

class ContactsViewModelData<C : JetIQViewModel>(data: List<UIReceiverDTO>, sender: KClass<C>) :
    ViewModelData<List<UIReceiverDTO>, C>(data, sender)