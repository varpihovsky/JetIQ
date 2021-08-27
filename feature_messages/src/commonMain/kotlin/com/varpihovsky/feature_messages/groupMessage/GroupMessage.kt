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
package com.varpihovsky.feature_messages.groupMessage

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.core_ui.compose.foundation.AlertDialog
import com.varpihovsky.feature_messages.contacts.ContactsComponent
import com.varpihovsky.feature_messages.contacts.ContactsScreen
import com.varpihovsky.feature_messages.contacts.chosen.ChosenContacts
import com.varpihovsky.feature_messages.field.MessageField

@Composable
internal fun GroupMessage(groupMessageComponent: GroupMessageComponent) {
    val contactsComponent by groupMessageComponent.contactsComponent

    if (LocalCompositionState.current.currentMode == Mode.Mobile) {
        groupMessageComponent.appBarController.run {
            show()
            setText("Групове повідомлення...")
            setIconToBack()
        }
    }

    when {
        // Show as separate screen
        contactsComponent != null && LocalCompositionState.current.currentMode == Mode.Mobile -> ContactsScreen(
            contactsComponent = contactsComponent!!, // It isn't null any way
            isMultiPane = false
        )
        else -> GroupMessagePane(groupMessageComponent, contactsComponent)
    }
}

@Composable
private fun GroupMessagePane(groupMessageComponent: GroupMessageComponent, contactsComponent: ContactsComponent?) {
    contactsComponent?.let {
        AlertDialog(
            modifier = Modifier.height(500.dp),
            onDismissRequest = groupMessageComponent::onDismissRequest,
            confirmButton = { TextButton(onClick = groupMessageComponent::onAcceptButtonClick) { Text("Прийняти") } },
            dismissButton = { IconButton(onClick = groupMessageComponent::onDismissRequest) { Text("Відхилити") } },
            title = { Text("Вибір контактів...") },
            text = {
                val scrollState = rememberScrollState()
                val offset = scrollState.value * LocalDensity.current.density

                ContactsScreen(
                    modifier = Modifier.offset(y = -offset.dp),
                    contactsComponent = it,
                    isMultiPane = false,
                    scrollState = scrollState
                )
            }
        )

    }

    SubcomposeLayout { constraints ->
        val chosenContacts = subcompose(Keys.CONTACTS) {
            ChosenContacts(chosenContactsComponent = groupMessageComponent.chosenContactsComponent)
        }.map { it.measure(constraints) }

        val field = subcompose(Keys.FIELD) {
            MessageField(messageFieldComponent = groupMessageComponent.messageFieldComponent)
        }.map { it.measure(constraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            chosenContacts.forEach { it.place(0, 0) }
            field.forEach { placeable ->
                placeable.place(0, constraints.maxHeight - (field.maxOfOrNull { it.height } ?: 0))
            }
        }
    }
}

private enum class Keys { CONTACTS, FIELD }