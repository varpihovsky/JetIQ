package com.varpihovsky.jetiq.screens.messages.contacts

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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core.util.selectedOnly
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.screens.messages.contacts.addition.AdditionDialog
import com.varpihovsky.jetiq.ui.compose.Avatar
import com.varpihovsky.jetiq.ui.compose.BackIconButton
import com.varpihovsky.jetiq.ui.compose.MapLifecycle
import com.varpihovsky.jetiq.ui.compose.getSizeByDensity
import com.varpihovsky.ui_data.dto.UIReceiverDTO

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun ContactsScreen(
    contactsViewModel: ContactsViewModel
) {
    val contacts = contactsViewModel.data.contacts.collectAsState(listOf()).value

    ContactsAppbar(
        contactsViewModel = contactsViewModel,
        isChoosing = contactsViewModel.data.isChoosing.value,
        isExternalChoosing = contactsViewModel.data.isExternalChoosing.value,
        contacts = contacts
    )

    MapLifecycle(viewModel = contactsViewModel)

    BackHandler(
        enabled = true,
        onBack = contactsViewModel::onBackNavButtonClick
    )

    if (contactsViewModel.data.isAdding.value) {
        AdditionDialog(
            contactAdditionViewModel = viewModel(),
            onDismissRequest = contactsViewModel::onDismissRequest,
            onConfirmButtonClick = contactsViewModel::onConfirmButtonClick
        )
    }

    ContactList(
        searchFieldValue = contactsViewModel.data.searchFieldValue.collectAsState().value,
        onSearchFieldValueChange = contactsViewModel::onSearchFieldValueChange,
        contacts = contacts,
        isLongClickEnabled = contactsViewModel.data.isLongClickEnabled.value,
        onLongClick = contactsViewModel::onContactLongClick,
        isClickEnabled = contactsViewModel.data.isClickEnabled.value,
        onClick = contactsViewModel::onContactClick
    )
}

@Composable
fun ContactsAppbar(
    contactsViewModel: ContactsViewModel,
    isChoosing: Boolean,
    isExternalChoosing: Boolean,
    contacts: List<Selectable<UIReceiverDTO>>
) {
    val text = if (isChoosing) "Вибір контактів..." else "Контакти"
    contactsViewModel.assignAppbar(
        title = text,
        icon = { BackIconButton(onClick = contactsViewModel::onBackNavButtonClick) },
        actions = {
            IconButton(onClick = contactsViewModel::onAddClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Додати")
            }
            if (!isExternalChoosing) {
                IconButton(
                    onClick = contactsViewModel::onRemoveClick,
                    enabled = contacts.selectedOnly().isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Видалити")
                }
            }
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun ContactList(
    searchFieldValue: String,
    onSearchFieldValueChange: (String) -> Unit,
    contacts: List<Selectable<UIReceiverDTO>>,
    isLongClickEnabled: Boolean,
    onLongClick: (Selectable<UIReceiverDTO>) -> Unit,
    isClickEnabled: Boolean,
    onClick: (Selectable<UIReceiverDTO>) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        stickyHeader(null) {
            SearchBar(
                searchFieldValue = searchFieldValue,
                onSearchFieldValueChange = onSearchFieldValueChange
            )
        }
        items(count = contacts.size) {
            if (it != 0) {
                Divider(Modifier.fillMaxWidth())
            }
            Contact(
                modifier = Modifier.padding(4.dp),
                contact = contacts[it],
                isLongClickEnabled = isLongClickEnabled,
                onLongClick = onLongClick,
                isClickEnabled = isClickEnabled,
                onClick = onClick
            )
            if (it != contacts.size) {
                Divider(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SearchBar(
    searchFieldValue: String,
    onSearchFieldValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(15.dp),
            value = searchFieldValue,
            onValueChange = onSearchFieldValueChange,
            shape = RoundedCornerShape(35.dp),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "ПІБ",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun Contact(
    modifier: Modifier = Modifier,
    contact: Selectable<UIReceiverDTO>,
    isLongClickEnabled: Boolean,
    onLongClick: (Selectable<UIReceiverDTO>) -> Unit,
    isClickEnabled: Boolean,
    onClick: (Selectable<UIReceiverDTO>) -> Unit
) {

    val startPadding = animateDpAsState(targetValue = if (contact.isSelected) 10.dp else 0.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                role = Role.Button,
                enabled = isClickEnabled || isLongClickEnabled,
                onClick = { if (isClickEnabled) onClick(contact) },
                onLongClick = { if (isLongClickEnabled) onLongClick(contact) }
            )
            .padding(start = startPadding.value),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .padding(10.dp)
        ) {
            if (contact.isSelected) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Green.copy(alpha = 0.2f))
                        .zIndex(10f),
                    painter = painterResource(id = R.drawable.ic_baseline_check_24),
                    contentDescription = null
                )
            }
            Avatar(
                modifier = Modifier.fillMaxSize(),
                url = contact.dto.getPhotoURL(),
                size = getSizeByDensity(size = 45)
            )
        }
        Text(
            modifier = Modifier.padding(10.dp),
            text = contact.dto.text,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
        )
    }
}