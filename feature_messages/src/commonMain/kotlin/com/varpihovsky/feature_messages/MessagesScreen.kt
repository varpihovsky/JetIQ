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
package com.varpihovsky.feature_messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.Value
import com.varpihovsky.feature_messages.chat.Chat
import com.varpihovsky.feature_messages.contacts.ContactsScreen
import com.varpihovsky.feature_messages.messaging.GroupMessage
import com.varpihovsky.feature_messages.wall.MessageWall

private const val MULTIPANE_WIDTH_THRESHOLD = 800
private const val MAIN_PANE_WEIGH = 0.30f
private const val DETAILS_PANE_WEIGHT = 0.70f
private const val SINGLE_PANE_WEIGHT = 1f

@Composable
fun MessagesScreen(messagesRootComponent: MessagesRootComponent) {
    val isMultiPane by messagesRootComponent.isMultiPane.subscribeAsState()

    BoxWithConstraints {
        val isMultiPaneRequired = maxWidth >= MULTIPANE_WIDTH_THRESHOLD.dp

        LaunchedEffect(isMultiPaneRequired) {
            messagesRootComponent.setMultiPane(isMultiPaneRequired)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(if (isMultiPane) MAIN_PANE_WEIGH else SINGLE_PANE_WEIGHT)) {
                MainPane(
                    routerState = messagesRootComponent.mainRouterState,
                    isMultiPane = isMultiPane
                )
            }

            if (isMultiPane) {
                Box(modifier = Modifier.weight(DETAILS_PANE_WEIGHT))
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (isMultiPane) {
                Box(modifier = Modifier.weight(MAIN_PANE_WEIGH))
            }

            Box(modifier = Modifier.weight(if (isMultiPane) DETAILS_PANE_WEIGHT else SINGLE_PANE_WEIGHT)) {
                DetailsPane(
                    routerState = messagesRootComponent.detailsRouterState,
                    isMultiPane = isMultiPane
                )
            }
        }

    }
}

@Composable
private fun MainPane(routerState: Value<RouterState<*, MessagesRootComponent.MainChild>>, isMultiPane: Boolean) {
    Children(
        routerState = routerState
    ) {
        when (val child = it.instance) {
            is MessagesRootComponent.MainChild.None -> Box {}
            is MessagesRootComponent.MainChild.Wall -> MessageWall(child.component)
            is MessagesRootComponent.MainChild.Contacts -> ContactsScreen(child.component, isMultiPane)
        }
    }
}

@Composable
private fun DetailsPane(routerState: Value<RouterState<*, MessagesRootComponent.DetailsChild>>, isMultiPane: Boolean) {
    Children(
        routerState = routerState
    ) {
        when (val child = it.instance) {
            is MessagesRootComponent.DetailsChild.None -> Box {}
            is MessagesRootComponent.DetailsChild.Chat -> Chat(child.component)
            is MessagesRootComponent.DetailsChild.GroupMessage -> GroupMessage(child.component)
            is MessagesRootComponent.DetailsChild.Contacts -> ContactsScreen(child.component, isMultiPane)
        }

    }
}