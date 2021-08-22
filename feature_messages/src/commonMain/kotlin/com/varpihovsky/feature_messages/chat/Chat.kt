package com.varpihovsky.feature_messages.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.varpihovsky.feature_messages.field.MessageField
import com.varpihovsky.feature_messages.messaging.Messaging

@Composable
internal fun Chat(chatComponent: ChatComponent) {
    Column(modifier = Modifier.fillMaxSize()) {
        Messaging(
            modifier = Modifier.weight(0.90f),
            messagingComponent = chatComponent.messagingComponent
        )
        MessageField(
            modifier = Modifier.weight(0.1f),
            messageFieldComponent = chatComponent.messageFieldComponent
        )
    }
}