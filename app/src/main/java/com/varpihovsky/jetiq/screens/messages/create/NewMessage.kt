package com.varpihovsky.jetiq.screens.messages.create

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.varpihovsky.jetiq.ui.compose.Avatar
import com.varpihovsky.jetiq.ui.compose.BasicAppBar
import com.varpihovsky.jetiq.ui.compose.CollectExceptions
import com.varpihovsky.jetiq.ui.compose.InfoCard
import com.varpihovsky.jetiq.ui.dto.ReceiverType
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import com.varpihovsky.jetiq.ui.theme.JetIQTheme

@Preview
@Composable
fun NewMessageScreenPreviewLight() {
    val messageFieldValue = remember { mutableStateOf("") }

    JetIQTheme(darkTheme = false) {
        NewMessageScreen(
            scrollState = rememberScrollState(),
            receivers = listOf(
                UIReceiverDTO(1, "Some Person", ReceiverType.TEACHER),
                UIReceiverDTO(2, "Some PersonSome Person", ReceiverType.TEACHER),
                UIReceiverDTO(
                    3,
                    "Some PersonSome PersonSome PersonSome Person",
                    ReceiverType.TEACHER
                ),
                UIReceiverDTO(4, "Some PersonSome Person", ReceiverType.TEACHER),
                UIReceiverDTO(
                    5,
                    "Some PersonSome PersonSome PersonSome PersonSome Person",
                    ReceiverType.TEACHER
                )
            ),
            onReceiverRemove = {},
            onNewReceiverButtonClick = {},
            messageFieldValue = messageFieldValue.value,
            onMessageValueChange = { messageFieldValue.value = it },
            onSendClick = {}
        )
    }
}

@Composable
fun NewMessageScreen(
    newMessageViewModel: NewMessageViewModel
) {
    val receivers = newMessageViewModel.data.receivers.observeAsState(initial = listOf())
    val messageFieldValue = newMessageViewModel.data.messageFieldValue.observeAsState(initial = "")

    newMessageViewModel.assignAppbar {
        BasicAppBar(
            title = "Нове повідомлення...",
            onBackClick = newMessageViewModel::onBackNavButtonClick
        )
    }

    CollectExceptions(viewModel = newMessageViewModel)

    NewMessageScreen(
        scrollState = rememberScrollState(),
        receivers = receivers.value,
        onReceiverRemove = newMessageViewModel::onReceiverRemove,
        onNewReceiverButtonClick = newMessageViewModel::onNewReceiverButtonClick,
        messageFieldValue = messageFieldValue.value,
        onMessageValueChange = newMessageViewModel::onMessageValueChange,
        onSendClick = newMessageViewModel::onSendClick
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
                url = receiver.getPhotoURL()
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
