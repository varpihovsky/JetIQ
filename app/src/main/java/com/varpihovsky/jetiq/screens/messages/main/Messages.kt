package com.varpihovsky.jetiq.screens.messages.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.ui.compose.InfoCard
import com.varpihovsky.jetiq.ui.compose.MapLifecycle
import com.varpihovsky.jetiq.ui.loremIpsum1Paragraph
import com.varpihovsky.jetiq.ui.loremIpsumTitle
import com.varpihovsky.jetiq.ui.sampleDate
import com.varpihovsky.ui_data.UIMessageDTO

private val exampleMessage = UIMessageDTO(0, loremIpsumTitle, loremIpsum1Paragraph, sampleDate)

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MessagesScreen(viewModel: MessagesViewModel) {
    MapLifecycle(viewModel = viewModel)

    BackHandler(true, onBack = viewModel::onBackNavButtonClick)

    viewModel.assignAppbar(
        title = "Повідомлення",
        icon = null,
        actions = {
            IconButton(onClick = viewModel::onContactsClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = null
                )
            }
        }
    )

    MessagesScreen(
        messages = viewModel.data.messages.value,
        onClick = viewModel::onNewMessageButtonClick,
        refreshState = rememberSwipeRefreshState(isRefreshing = viewModel.isLoading.value),
        onRefresh = viewModel::onRefresh
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MessagesScreen(
    messages: List<UIMessageDTO>,
    onClick: () -> Unit,
    refreshState: SwipeRefreshState,
    onRefresh: () -> Unit
) {
    SwipeRefresh(state = refreshState, onRefresh = onRefresh) {
        MessagesList(messages = messages)
    }
    NewMessageButton(onClick = onClick)
}

@ExperimentalAnimationApi
@Composable
fun MessagesList(messages: List<UIMessageDTO>) {
    val scope = rememberCoroutineScope()
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        state = state
    ) {
        items(messages.size) {
            AnimatedMessageItem(
                message = messages[it],
                true
            )
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun AnimatedMessageItem(message: UIMessageDTO, visibility: Boolean) {
    AnimatedVisibility(visible = visibility, modifier = Modifier.wrapContentSize()) {
        InfoCard(
            modifier = Modifier.padding(vertical = 5.dp),
            cardModifier = Modifier.fillMaxWidth(0.94f)
        ) {
            MessageItem(message = message)
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
            SelectionContainer {
                Text(text = message.message, style = MaterialTheme.typography.body1)
            }
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