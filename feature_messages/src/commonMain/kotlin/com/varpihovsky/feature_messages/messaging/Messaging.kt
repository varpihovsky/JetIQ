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
package com.varpihovsky.feature_messages.messaging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.core_ui.compose.entities.MessageItem
import com.varpihovsky.ui_data.dto.UIMessageDTO
import kotlin.math.max

const val maxMessageSize = 0.6f
const val minMessageSize = 0.1f

@Composable
internal fun Messaging(modifier: Modifier = Modifier, messagingComponent: MessagingComponent) {
    val state by messagingComponent.state.collectAsState(listOf())

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(state.size) {
            Message(state[it])
        }
    }
}

@Composable
private fun Message(message: MessagingComponent.Message) {
    val size = calculateMessageSize(message.body, message.title)

    val messageComposable = @Composable {
        MessageItem(UIMessageDTO(0, message.title, message.body, message.timeToString()))
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        when (message.type) {
            MessagingComponent.Message.Type.Your -> {
                Box(modifier = Modifier.weight(1f - size))
                Box(modifier = Modifier.weight(size)) {
                    messageComposable()
                }
            }
            MessagingComponent.Message.Type.Others -> {
                Box(modifier = Modifier.weight(size)) {
                    messageComposable()
                }
                Box(modifier = Modifier.weight(1f - size))
            }
        }
    }
}

private fun calculateMessageSize(message: String, title: String): Float {
    val title = title.length / 40f
    val message = message.length / 34f

    var size = max(message, title)

    if (LocalCompositionState.current.currentMode is Mode.Mobile) {
        size *= 1.5f
    }

    if (size > maxMessageSize) {
        size = maxMessageSize
    }
    if (size < minMessageSize) {
        size = minMessageSize
    }

    return size
}