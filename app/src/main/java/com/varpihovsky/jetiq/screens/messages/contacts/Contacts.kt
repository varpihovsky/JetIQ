package com.varpihovsky.jetiq.screens.messages.contacts

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.screens.messages.contacts.addition.AdditionDialog
import com.varpihovsky.jetiq.ui.compose.Avatar
import com.varpihovsky.jetiq.ui.compose.BackIconButton
import com.varpihovsky.jetiq.ui.dto.UIReceiverDTO
import com.varpihovsky.jetiq.ui.dto.func_extensions.Selectable

@ExperimentalFoundationApi
@Composable
fun ContactsScreen(
    contactsViewModel: ContactsViewModel
) {
    val searchFieldValue = contactsViewModel.data.searchFieldValue.observeAsState(initial = "")
    val contacts = contactsViewModel.data.contacts.observeAsState(initial = listOf())
    val isClickEnabled = contactsViewModel.data.isClickEnabled.observeAsState(initial = false)
    val isLongClickEnabled =
        contactsViewModel.data.isLongClickEnabled.observeAsState(initial = true)
    val isChoosing = contactsViewModel.data.isChoosing.observeAsState(initial = false)
    val isExternalChoosing =
        contactsViewModel.data.isExternalChoosing.observeAsState(initial = false)
    val isAdding = contactsViewModel.data.isAdding.observeAsState(initial = false)

    contactsViewModel.assignAppbar {
        ContactsAppbar(
            contactsViewModel = contactsViewModel,
            isChoosing = isChoosing.value,
            isExternalChoosing = isExternalChoosing.value
        )
    }

    BackHandler(
        enabled = true,
        onBack = contactsViewModel::onBackNavButtonClick
    )

    if (isAdding.value) {
        AdditionDialog(
            contactAdditionViewModel = hiltViewModel(),
            onDismissRequest = contactsViewModel::onDismissRequest,
            onConfirmButtonClick = contactsViewModel::onConfirmButtonClick
        )
    }

    ContactList(
        searchFieldValue = searchFieldValue.value,
        onSearchFieldValueChange = contactsViewModel::onSearchFieldValueChange,
        contacts = contacts.value,
        isLongClickEnabled = isLongClickEnabled.value,
        onLongClick = contactsViewModel::onContactLongClick,
        isClickEnabled = isClickEnabled.value,
        onClick = contactsViewModel::onContactClick
    )
}

@Composable
fun ContactsAppbar(
    contactsViewModel: ContactsViewModel,
    isChoosing: Boolean,
    isExternalChoosing: Boolean
) {
    val text = if (isChoosing) "Вибір контактів..." else "Контакти"
    TopAppBar(
        title = { Text(text = text) },
        navigationIcon = { BackIconButton(onClick = contactsViewModel::onBackNavButtonClick) },
        actions = {
            IconButton(onClick = contactsViewModel::onAddClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Додати")
            }
            if (!isExternalChoosing) {
                IconButton(onClick = contactsViewModel::onRemoveClick) {
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
                enabled = isClickEnabled || isLongClickEnabled,
                onClick = { onClick(contact) },
                onLongClick = { onLongClick(contact) }
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
                url = contact.dto.getPhotoURL()
            )
        }
        Text(
            modifier = Modifier.padding(10.dp),
            text = contact.dto.text,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
        )
    }
}