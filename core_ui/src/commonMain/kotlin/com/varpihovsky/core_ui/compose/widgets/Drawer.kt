package com.varpihovsky.core_ui.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun DrawerItem(
    text: String,
    icon: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.padding(7.dp).clickable(onClick = onClick)) {
        Icon(
            modifier = Modifier.padding(4.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(text = text, style = MaterialTheme.typography.h6)
    }
}