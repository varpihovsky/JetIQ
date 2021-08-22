/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.varpihovsky.feature_new_message

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core_ui.compose.widgets.Avatar
import com.varpihovsky.core_ui.compose.widgets.InfoCard
import com.varpihovsky.ui_data.dto.ReceiverType
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import com.varpihovsky.ui_data.dto.getPhotoURL

@Composable
fun NewMessageScreen(newMessageComponent: NewMessageComponent) {
    newMessageComponent.appBarController.run {
        show()
        setText("Нове повідомлення...")
        setIconToBack()
    }

    val receivers by newMessageComponent.receivers.subscribeAsState()
    val messageFieldValue by newMessageComponent.messageFieldValue.subscribeAsState()

    NewMessageScreen(
        scrollState = rememberScrollState(),
        receivers = receivers,
        onReceiverRemove = newMessageComponent::onReceiverRemove,
        onNewReceiverButtonClick = newMessageComponent::onNewReceiverButtonClick,
        messageFieldValue = messageFieldValue,
        onMessageValueChange = newMessageComponent::onMessageValueChange,
        onSendClick = newMessageComponent::onSendClick
    )
}

@Composable
private fun NewMessageScreen(
    scrollState: ScrollState,
    receivers: List<UIReceiverDTO>,
    onReceiverRemove: (UIReceiverDTO) -> Unit,
    onNewReceiverButtonClick: () -> Unit,
    messageFieldValue: String,
    onMessageValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        InfoCard {
            Column {
                ChosenReceivers(
                    receivers = receivers,
                    onReceiverRemove = onReceiverRemove,
                    onNewReceiverButtonClick = onNewReceiverButtonClick
                )
                Divider(modifier = Modifier.fillMaxWidth())
                MessageField(
                    modifier = Modifier.padding(5.dp),
                    fieldValue = messageFieldValue,
                    onValueChange = onMessageValueChange,
                    onSendClick = onSendClick
                )
            }
        }
    }
}

@Composable
fun ChosenReceivers(
    receivers: List<UIReceiverDTO>,
    onReceiverRemove: (UIReceiverDTO) -> Unit,
    onNewReceiverButtonClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        receivers.forEach {
            Receiver(
                modifier = Modifier.fillMaxWidth(),
                receiver = it,
                icon = Icons.Default.Delete,
                onClick = onReceiverRemove,
                contentDescription = "Видалити"
            )
        }
        Divider(modifier = Modifier.fillMaxWidth())
        AddReceiver(modifier = Modifier.fillMaxWidth(), onClick = onNewReceiverButtonClick)
    }
}

@Composable
fun AddReceiver(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Receiver(
        modifier = modifier,
        receiver = UIReceiverDTO(-1, "Додати одержувача...", ReceiverType.STUDENT),
        icon = Icons.Default.Add,
        contentDescription = "Додати",
        onClick = { onClick() }
    )
}

@Composable
fun Receiver(
    modifier: Modifier = Modifier,
    receiver: UIReceiverDTO,
    icon: ImageVector,
    contentDescription: String?,
    onClick: ((UIReceiverDTO) -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .animateContentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(4f, true),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                modifier = Modifier
                    .size(65.dp)
                    .padding(end = 10.dp),
                url = receiver.getPhotoURL(),
            )
            Text(
                text = receiver.text,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
            )
        }
        onClick?.let {
            IconButton(
                modifier = Modifier.weight(1f, true),
                onClick = { it(receiver) }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = icon,
                    contentDescription = contentDescription
                )
            }
        }
    }
}

@Composable
fun MessageField(
    modifier: Modifier = Modifier,
    fieldValue: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(modifier = Modifier.weight(4f), value = fieldValue, onValueChange = onValueChange)
        IconButton(modifier = Modifier.weight(1f), onClick = onSendClick) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Відправити")
        }
    }
}
