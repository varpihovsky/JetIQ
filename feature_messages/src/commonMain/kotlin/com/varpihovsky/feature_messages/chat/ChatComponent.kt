package com.varpihovsky.feature_messages.chat

import com.arkivanov.decompose.value.MutableValue
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.feature_messages.childContext
import com.varpihovsky.feature_messages.field.MessageFieldComponent
import com.varpihovsky.feature_messages.messaging.MessagingComponent
import com.varpihovsky.ui_data.dto.UIReceiverDTO

internal class ChatComponent(
    messagesComponentContext: MessagesComponentContext,
    val contact: UIReceiverDTO
) : MessagesComponentContext by messagesComponentContext {
    val messageFieldComponent = if (contact.id == -1) null
    else MessageFieldComponent(childContext("MessageFieldComponent"), MutableValue(listOf(contact)))

    val messagingComponent = MessagingComponent(childContext("MessagingComponent"), contact.id, contact.type.toInt())
}