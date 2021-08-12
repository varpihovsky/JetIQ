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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.varpihovsky.core_ui.R

@Composable
internal actual fun DropDownMenuActual(
    modifier: Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        content = content
    )
}

@Composable
internal actual fun DropDownMenuItemActual(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    DropdownMenuItem(
        modifier = modifier,
        onClick = onClick,
        content = content
    )
}

@Composable
actual fun PasswordFieldIcon(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange
    ) {
        if (checked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_remove_red_eye_24),
                contentDescription = null
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_password_24),
                contentDescription = null
            )
        }
    }
}