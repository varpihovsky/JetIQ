package com.varpihovsky.feature_messages.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.varpihovsky.feature_messages.field.MessageField
import com.varpihovsky.feature_messages.messaging.Messaging

@Composable
internal fun Chat(chatComponent: ChatComponent) {
    Card {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(0.9f, true)) {
                Messaging(messagingComponent = chatComponent.messagingComponent)
            }
            chatComponent.messageFieldComponent?.let {
                Box(modifier = Modifier.weight(0.1f, true)) {
                    MessageField(messageFieldComponent = it)
                }
            }
        }
    }
}