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
package com.varpihovsky.core_ui.compose.foundation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.widgets.BasicTextButton

@Composable
actual fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            CenterLayoutItem {
                BasicTextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = onDismiss,
                    text = "Відхилити"
                )
            }
        },
        title = {
            CenterLayoutItem {
                Text(modifier = Modifier.padding(10.dp), text = "Помилка")
            }
        },
        text = {
            CenterLayoutItem {
                Text(modifier = Modifier.padding(10.dp), text = message)
            }
        }
    )
}