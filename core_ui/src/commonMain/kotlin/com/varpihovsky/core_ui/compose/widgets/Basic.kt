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
package com.varpihovsky.core_ui.compose.widgets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BackIconButton(
    onClick: () -> Unit
) {
    androidx.compose.material.IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    androidx.compose.material.IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "settings",
        )
    }
}

@Composable
fun BasicTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    TextButton(modifier = modifier, onClick = onClick) {
        Text(text = text)
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    androidx.compose.material.IconButton(modifier = modifier, onClick = onClick) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Composable
expect fun PasswordFieldIcon(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
)

@Composable
expect fun DropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
)

@Composable
expect fun DropDownMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
)