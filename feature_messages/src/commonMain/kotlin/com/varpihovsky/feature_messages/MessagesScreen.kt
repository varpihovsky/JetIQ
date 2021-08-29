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

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.core_ui.compose.widgets.DrawerItem
import com.varpihovsky.feature_messages.chat.Chat
import com.varpihovsky.feature_messages.contacts.ContactsScreen
import com.varpihovsky.feature_messages.contacts.addition.AdditionDialog
import com.varpihovsky.feature_messages.groupMessage.GroupMessage
import com.varpihovsky.feature_messages.wall.MessageWall

private const val MULTIPANE_WIDTH_THRESHOLD = 800

@Composable
fun MessagesScreen(messagesRootComponent: MessagesRootComponent) {
    val isMultiPane by messagesRootComponent.isMultiPane.subscribeAsState()

    if (LocalCompositionState.current.currentMode is Mode.Desktop) {
        messagesRootComponent.appBarController.run {
            show()
            setText("Повідомлення")
            setIconToDrawer()
            setActions { }
        }
        messagesRootComponent.drawerController.setNavigation(text = "Повідомлення") {
            DrawerItem(
                text = "Новий контакт...",
                icon = rememberVectorPainter(Icons.Default.Add),
                onClick = messagesRootComponent::newContactDialog
            )
            DrawerItem(
                text = "Групове повідомлення...",
                icon = rememberVectorPainter(Icons.Default.Message),
                onClick = messagesRootComponent::navigateToGroupMessage
            )
        }
    }

    messagesRootComponent.contactAdditionComponent.value?.let {
        AdditionDialog(
            contactAdditionComponent = it,
            onDismissRequest = messagesRootComponent::onDismissRequest,
            onConfirmButtonClick = messagesRootComponent::onConfirmButtonClick
        )
    }

    BoxWithConstraints {
        val isMultiPaneRequired = maxWidth >= MULTIPANE_WIDTH_THRESHOLD.dp

        LaunchedEffect(isMultiPaneRequired) {
            messagesRootComponent.setMultiPane(isMultiPaneRequired)
        }

        val currentWidth = maxWidth
        val mainPaneWidth = when {
            currentWidth > 800.dp && currentWidth < 1000.dp -> 250.dp
            currentWidth > 1000.dp && currentWidth < 1400.dp -> 330.dp
            else -> 430.dp
        }

        val mainPaneModifier = if (isMultiPane) Modifier.width(mainPaneWidth) else Modifier.fillMaxSize()
        val detailsPaneModifier =
            if (isMultiPane) Modifier.width(currentWidth - mainPaneWidth) else Modifier.fillMaxSize()

        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = mainPaneModifier) {
                MainPane(
                    routerState = messagesRootComponent.mainRouterState,
                    isMultiPane = isMultiPane
                )
            }

            if (isMultiPane) {
                Box(modifier = detailsPaneModifier)
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (isMultiPane) {
                Box(modifier = mainPaneModifier)
            }

            Box(modifier = detailsPaneModifier) {
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
            is MessagesRootComponent.MainChild.Contacts -> ContactsScreen(
                contactsComponent = child.component
            )
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
        }
    }
}