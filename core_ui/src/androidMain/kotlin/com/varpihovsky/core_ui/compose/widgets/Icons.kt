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

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.varpihovsky.core_ui.R

@Composable
actual fun PersonIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_person_24),
        contentDescription = null,
        tint = tint
    )
}


@Composable
actual fun RedEyeIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_remove_red_eye_24),
        contentDescription = null,
        tint = tint
    )
}

@Composable
actual fun PasswordIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_password_24),
        contentDescription = null,
        tint = tint
    )
}

@Composable
actual fun CheckIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_check_24),
        contentDescription = null,
        tint = tint
    )
}

@Composable
actual fun MessageIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_message_24),
        contentDescription = null,
        tint = tint
    )
}

@Composable
actual fun HelpIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_help_24),
        contentDescription = null,
        tint = tint
    )
}

@Composable
actual fun LogoutIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_logout_24),
        contentDescription = null,
        tint = tint
    )
}

@Composable
actual fun SettingsApplicationsIcon(tint: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_settings_applications_24),
        contentDescription = null,
        tint = tint
    )
}