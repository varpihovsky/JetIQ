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
package com.varpihovsky.feature_messages.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.feature_messages.field.MessageField
import com.varpihovsky.feature_messages.messaging.Messaging

@Composable
internal fun Chat(chatComponent: ChatComponent) {
    if (LocalCompositionState.current.currentMode is Mode.Mobile) {
        chatComponent.appBarController.run {
            show()
            setText("Чат")
            setIconToBack()
        }
        chatComponent.bottomBarController.hide()
    }

    SubcomposeLayout { constraints ->
        val field = chatComponent.messageFieldComponent?.let {
            subcompose(Keys.FIELD) {
                MessageField(messageFieldComponent = it)
            }.map { it.measure(constraints) }
        }

        val messaging = subcompose(Keys.MESSAGING) {
            Messaging(
                modifier = Modifier.padding(
                    bottom = field?.maxOfOrNull { it.height }?.toDp() ?: 0.dp
                ),
                messagingComponent = chatComponent.messagingComponent
            )
        }.map { it.measure(constraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            field?.forEach { it.place(0, constraints.maxHeight - (field.maxOfOrNull { it.height } ?: 0)) }
            messaging.forEach { it.place(0, 0) }
        }
    }
}

private enum class Keys { MESSAGING, FIELD }