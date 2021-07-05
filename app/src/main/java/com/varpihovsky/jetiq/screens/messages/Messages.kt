package com.varpihovsky.jetiq.screens.messages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.ui.compose.CollectExceptions
import com.varpihovsky.jetiq.ui.compose.InfoCard
import com.varpihovsky.jetiq.ui.dto.UIMessageDTO
import com.varpihovsky.jetiq.ui.loremIpsum1Paragraph
import com.varpihovsky.jetiq.ui.loremIpsumTitle
import com.varpihovsky.jetiq.ui.sampleDate
import com.varpihovsky.jetiq.ui.theme.JetIQTheme

private val exampleMessage = UIMessageDTO(0, loremIpsumTitle, loremIpsum1Paragraph, sampleDate)

@ExperimentalFoundationApi
@Preview
@Composable
fun MessagesPreviewLight() {
    JetIQTheme(darkTheme = false) {
        MessagesScreen(listOf(exampleMessage, exampleMessage, exampleMessage, exampleMessage)) {}
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun MessagesPreviewDark() {
    JetIQTheme(darkTheme = true) {
        MessagesScreen(listOf(exampleMessage, exampleMessage, exampleMessage, exampleMessage)) {}
    }
}

@ExperimentalFoundationApi
@Composable
fun MessagesScreen(viewModel: MessagesViewModel) {
    DisposableEffect(key1 = viewModel) {
        viewModel.onCompose()
        onDispose(viewModel::onDispose)
    }

    val messagesState by viewModel.data.messages.observeAsState()
    val messages = messagesState ?: emptyList()

    CollectExceptions(viewModel = viewModel)

    MessagesScreen(messages = messages, onClick = viewModel::onNewMessageButtonClick)
}

@ExperimentalFoundationApi
@Composable
fun MessagesScreen(messages: List<UIMessageDTO>, onClick: () -> Unit) {
    MessagesList(messages = messages)
    NewMessageButton(onClick = onClick)
}

@Composable
fun MessagesList(messages: List<UIMessageDTO>) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        items(messages.size) {
            InfoCard(
                modifier = Modifier.padding(vertical = 5.dp),
                cardModifier = Modifier.fillMaxWidth(0.94f)
            ) {
                MessageItem(message = messages[it])
            }
        }
    }
}

@Composable
fun NewMessageButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(onClick = onClick, modifier = Modifier.padding(20.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_message_24),
                contentDescription = "Write a message",
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun MessagePreview() {
    InfoCard {
        MessageItem(exampleMessage)
    }
}

@Composable
fun MessageItem(message: UIMessageDTO) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, top = 5.dp)
        ) {
            Text(text = message.sender, style = MaterialTheme.typography.h6)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp, vertical = 4.dp)
        ) {
            Text(text = message.message, style = MaterialTheme.typography.body1)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = message.time, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun MessageCard(
    content: @Composable () -> Unit
) {
    InfoCard(
        modifier = Modifier.padding(10.dp)
    ) {
        content()
    }
}