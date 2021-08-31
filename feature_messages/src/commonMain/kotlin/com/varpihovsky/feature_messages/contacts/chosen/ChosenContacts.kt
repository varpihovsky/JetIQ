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
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core.log.Logger
import com.varpihovsky.core.log.d
import com.varpihovsky.ui_data.dto.ReceiverType
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import kotlin.math.roundToInt

@Composable
internal fun ChosenContacts(
    modifier: Modifier = Modifier,
    chosenContactsComponent: ChosenContactsComponent
) {
    val contacts by chosenContactsComponent.contacts.subscribeAsState()

    Logger.ui(contacts.size.toString())

    if (contacts.isEmpty()) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactChips(
                contact = UIReceiverDTO(
                    id = -1,
                    "Одержувачі пусті...",
                    type = ReceiverType.STUDENT
                ),
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
    SubcomposeLayout(modifier = modifier) { constraints ->
        constraints.d(constraints.toString())

        val paddingY = 15.toDp().value.roundToInt()
        val paddingX = 10.toDp().value.roundToInt()

        var currentX = paddingX
        var currentY = paddingY

        val placeables = contacts.mapIndexed { index, uiReceiverDTO ->
            subcompose(index) {
                ContactChips(
                    contact = uiReceiverDTO,
                    onRemove = onRemove
                )
            }.map {
                FlowLayoutData(
                    placeable = it.measure(constraints),
                    x = currentX,
                    y = currentY
                ).also {
                    currentX += it.placeable.width + paddingX
                    if (currentX + paddingX + it.placeable.width > constraints.maxWidth) {
                        currentX = paddingX
                        currentY += it.placeable.height + paddingY
                    }
                    it.d("${currentX}:${currentY}")
                }
            }
        }.onEach { it.d(it.toString()) }

        val addButton =
            subcompose(Keys.ADD_BUTTON) {
                AddButton(onAdd)
            }.map { it.measure(constraints) }.map {
                val x = placeables.last().maxOf { it.x + it.placeable.width + paddingX }
                val y = placeables.last().maxOf { it.y }
                it.d("$x $y")
                it.d("${it.width} ${it.height}")
                FlowLayoutData(it, x, y)
            }

        // We also need a shadow to be visible
        val shadowPadding = 35.toDp().value.roundToInt()

        val layoutHeight = placeables.last().maxOf { it.y + it.placeable.height }


        layout(constraints.maxWidth, layoutHeight + shadowPadding) {
            placeables.forEach { list ->
                list.forEach { it.placeable.place(it.x, it.y) }
            }
            addButton.forEach { it.placeable.place(it.x, it.y) }
        }
    }
}

private data class FlowLayoutData(val placeable: Placeable, val x: Int, val y: Int)

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