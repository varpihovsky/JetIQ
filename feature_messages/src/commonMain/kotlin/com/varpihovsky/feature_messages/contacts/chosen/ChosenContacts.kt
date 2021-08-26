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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.ui_data.dto.ReceiverType
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlin.math.roundToInt

@Composable
internal fun ChosenContacts(modifier: Modifier = Modifier, chosenContactsComponent: ChosenContactsComponent) {
    val contacts by chosenContactsComponent.contacts.subscribeAsState()

    if (contacts.isEmpty()) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactChips(
                contact = UIReceiverDTO(id = -1, "Одержувачі пусті...", type = ReceiverType.STUDENT),
                onRemove = {}
            )
            AddButton(onClick = chosenContactsComponent::onAddContactClick)
        }
    } else {
        FlowLayout(
            modifier = modifier,
            contacts = contacts,
            onRemove = chosenContactsComponent::onRemoveContactClick,
            onAdd = chosenContactsComponent::onAddContactClick
        )
    }
}

@Composable
private fun FlowLayout(
    modifier: Modifier,
    contacts: List<UIReceiverDTO>,
    onRemove: (UIReceiverDTO) -> Unit,
    onAdd: () -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints ->
        val placeables = contacts.mapIndexed { index, uiReceiverDTO ->
            subcompose(index) {
                ContactChips(
                    contact = uiReceiverDTO,
                    onRemove = onRemove
                )
            }.map { it.measure(constraints) }
        }

        val addButton = subcompose(Keys.ADD_BUTTON) { AddButton(onAdd) }.map { it.measure(constraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val paddingY = 15.toDp().value.roundToInt()
            val paddingX = 10.toDp().value.roundToInt()

            var currentX = paddingX
            var currentY = paddingY

            placeables.forEachIndexed { index, list ->
                list.forEach { it.place(currentX, currentY) }

                currentX += (list.maxOfOrNull { it.width } ?: 10) + paddingX

                val nextPlaceableWidth = placeables.getOrNull(index + 1)?.maxOfOrNull { it.width } ?: 0
                if (currentX + nextPlaceableWidth + paddingX > constraints.maxWidth) {
                    currentX = paddingX
                    currentY += (list.maxOfOrNull { it.height } ?: 10) + paddingY
                }
            }
            addButton.forEach { it.place(currentX, currentY) }
        }
    }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

private enum class Keys { ADD_BUTTON }

@Composable
private fun ContactChips(contact: UIReceiverDTO, onRemove: (UIReceiverDTO) -> Unit) {
    Card(
        shape = RoundedCornerShape(50),
        backgroundColor = MaterialTheme.colors.secondary,
        elevation = 12.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = contact.text,
                style = MaterialTheme.typography.subtitle1
            )

            if (contact.id != -1) {
                IconButton(
                    onClick = { onRemove(contact) }
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
}