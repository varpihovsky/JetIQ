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
package com.varpihovsky.feature_messages.wall

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.entities.MessageItem
import com.varpihovsky.core_ui.compose.foundation.SwipeRefresh
import com.varpihovsky.ui_data.dto.UIMessageDTO

@Composable
internal fun MessageWall(messagesComponent: MessagesComponent) {
    messagesComponent.appBarController.run {
        show()
        setText("Повідомлення")
        setActions {
            IconButton(onClick = messagesComponent::onContactsClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
        }
        hideIcon()
    }
    messagesComponent.bottomBarController.show()

    MessageWall(
        messages = messagesComponent.messages.collectAsState(initial = listOf()).value,
        onClick = messagesComponent::onNewMessageButtonClick,
        loadingState = messagesComponent.isLoading.value,
        onRefresh = messagesComponent::onRefresh
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MessageWall(
    messages: List<UIMessageDTO>,
    onClick: () -> Unit,
    loadingState: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(isRefreshing = loadingState, onRefresh = onRefresh) {
        MessagesList(messages = messages)
    }
    NewMessageButton(onClick = onClick)
}

@ExperimentalAnimationApi
@Composable
fun MessagesList(messages: List<UIMessageDTO>) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        state = state
    ) {
        if (messages.isEmpty()) {
            item {
                MessageItem(message = UIMessageDTO(0, "JetIQ", "Повідомлення відсутні.", ""))
            }
        }
        items(messages.size) {
            MessageItem(message = messages[it])
        }
    }
}

@Composable
fun NewMessageButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(onClick = onClick, modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}
