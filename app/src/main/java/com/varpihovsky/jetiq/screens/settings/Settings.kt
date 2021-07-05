package com.varpihovsky.jetiq.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(

) {

}

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    title: String,
    hint: String,
    additionalBlock: @Composable (() -> Unit)? = null,
    icon: Painter? = null,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable(
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = interactionSource
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        //.padding(end = 15.dp)
                        .weight(1f, true)
                        .size(35.dp),
                    painter = icon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colors.onBackground
                )
            }

            Column(modifier = Modifier.weight(4f, true)) {
                Text(text = title, style = MaterialTheme.typography.h6)
                Text(text = hint, style = MaterialTheme.typography.caption)
            }
            additionalBlock?.let {
                Row(modifier = Modifier.weight(1f, true)) {
                    it()
                }
            }
        }
    }
}

@Composable
fun SettingsAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}