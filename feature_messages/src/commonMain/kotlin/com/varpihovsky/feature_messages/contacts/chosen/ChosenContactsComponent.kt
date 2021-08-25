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
package com.varpihovsky.feature_messages.contacts.chosen

import com.arkivanov.decompose.value.Value
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.ui_data.dto.UIReceiverDTO

internal class ChosenContactsComponent(
    messagesComponentContext: MessagesComponentContext,
    val contacts: Value<List<UIReceiverDTO>>,
    private val onAddContactClick: () -> Unit,
    private val onRemoveContactClick: (UIReceiverDTO) -> Unit
) : MessagesComponentContext by messagesComponentContext {
    fun onAddContactClick() {
        onAddContactClick.invoke()
    }

    fun onRemoveContactClick(contact: UIReceiverDTO) {
        onRemoveContactClick.invoke(contact)
    }
}