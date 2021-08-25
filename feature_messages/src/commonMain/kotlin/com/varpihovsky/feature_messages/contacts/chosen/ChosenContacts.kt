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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.ui_data.dto.UIReceiverDTO

@Composable
internal fun ChosenContacts(modifier: Modifier = Modifier, chosenContactsComponent: ChosenContactsComponent) {
    val contacts by chosenContactsComponent.contacts.subscribeAsState()

    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(contacts.size) {
            ContactChips(
                contact = contacts[it],
                onRemove = chosenContactsComponent::onRemoveContactClick
            )
        }
        item {
            IconButton(
                onClick = chosenContactsComponent::onAddContactClick
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}

@Composable
private fun ContactChips(contact: UIReceiverDTO, onRemove: (UIReceiverDTO) -> Unit) {
    Card(
        shape = RoundedCornerShape(50),
        backgroundColor = MaterialTheme.colors.secondary,
        elevation = 12.dp
    ) {
        Row {
            Text(
                text = contact.text,
                style = MaterialTheme.typography.subtitle1
            )

            IconButton(
                onClick = { onRemove(contact) }
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}