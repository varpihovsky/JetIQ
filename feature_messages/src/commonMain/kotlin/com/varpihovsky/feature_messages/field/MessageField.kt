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
package com.varpihovsky.feature_messages.field

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
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
import com.varpihovsky.ui_data.dto.ReceiverType
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import com.varpihovsky.ui_data.dto.getPhotoURL

@Composable
internal fun MessageField(modifier: Modifier = Modifier, messageFieldComponent: MessageFieldComponent) {
    val messageFieldValue by messageFieldComponent.messageFieldValue.subscribeAsState()

    MessageField(
        modifier = modifier,
        messageFieldValue = messageFieldValue,
        onMessageValueChange = messageFieldComponent::onMessageValueChange,
        onSendClick = messageFieldComponent::onSendClick
    )
}

@Composable
private fun MessageField(
    modifier: Modifier,
    onMessageValueChange: (String) -> Unit,
    messageFieldValue: String,
    onSendClick: () -> Unit
) {
    Card {
        MessageField(
            modifier = modifier,
            fieldValue = messageFieldValue,
            onValueChange = onMessageValueChange,
            onSendClick = onSendClick
        )
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
private fun MessageField(
    modifier: Modifier = Modifier,
    fieldValue: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    BoxWithConstraints {
        val maxWidth = this.maxWidth
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(modifier = Modifier.width(maxWidth - 50.dp), value = fieldValue, onValueChange = onValueChange)
            IconButton(onClick = onSendClick) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Відправити")
            }
        }
    }
}
