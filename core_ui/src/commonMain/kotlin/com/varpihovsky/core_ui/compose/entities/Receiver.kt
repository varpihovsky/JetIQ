package com.varpihovsky.core_ui.compose.entities

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.varpihovsky.core.util.Selectable
import com.varpihovsky.core_ui.compose.widgets.Avatar
import com.varpihovsky.core_ui.compose.widgets.SearchBar
import com.varpihovsky.ui_data.dto.UIReceiverDTO
import com.varpihovsky.ui_data.dto.getPhotoURL

@ExperimentalFoundationApi
@Composable
fun ContactList(
    modifier: Modifier,
    searchFieldValue: String,
    onSearchFieldValueChange: (String) -> Unit,
    contacts: List<Selectable<UIReceiverDTO>>,
    onLongClick: (Selectable<UIReceiverDTO>) -> Unit,
    onClick: (Selectable<UIReceiverDTO>) -> Unit,
    scrollState: ScrollState
) {
    Column(modifier = modifier.fillMaxSize().verticalScroll(scrollState)) {
        SearchBar(
            searchFieldValue = searchFieldValue,
            onSearchFieldValueChange = onSearchFieldValueChange
        )
        contacts.forEachIndexed { index, selectable ->
            if (index != 0) {
                Divider(Modifier.fillMaxWidth())
            }
            Contact(
                modifier = Modifier.padding(4.dp),
                contact = selectable,
                onLongClick = onLongClick,
                onClick = onClick
            )
            if (index != contacts.size) {
                Divider(Modifier.fillMaxWidth())
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Contact(
    modifier: Modifier = Modifier,
    contact: Selectable<UIReceiverDTO>,
    onLongClick: (Selectable<UIReceiverDTO>) -> Unit,
    onClick: (Selectable<UIReceiverDTO>) -> Unit
) {
    val startPadding = animateDpAsState(targetValue = if (contact.isSelected) 10.dp else 0.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                role = Role.Button,
                enabled = true,
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
                    imageVector = Icons.Default.Check,
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