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

import com.arkivanov.decompose.value.MutableValue
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.feature_messages.childContext
import com.varpihovsky.feature_messages.field.MessageFieldComponent
import com.varpihovsky.feature_messages.messaging.MessagingComponent
import com.varpihovsky.ui_data.dto.UIReceiverDTO

internal class ChatComponent(
    messagesComponentContext: MessagesComponentContext,
    val contact: UIReceiverDTO
) : MessagesComponentContext by messagesComponentContext {
    val messageFieldComponent = if (contact.id == -1) null
    else MessageFieldComponent(childContext("MessageFieldComponent"), MutableValue(listOf(contact)))

    val messagingComponent = MessagingComponent(childContext("MessagingComponent"), contact.id, contact.type.toInt())
}