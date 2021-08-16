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
package com.varpihovsky.core_ui.compose

import androidx.compose.runtime.Composable

@Composable
actual fun OpenPage(url: String) {
    val os = System.getProperty("os.name").lowercase()
    when {
        os.contains("win") -> Runtime.getRuntime().exec("start $url")
        os.contains("mac") -> Runtime.getRuntime().exec("open $url")
        else -> Runtime.getRuntime().exec("xdg-open $url")
    }
}