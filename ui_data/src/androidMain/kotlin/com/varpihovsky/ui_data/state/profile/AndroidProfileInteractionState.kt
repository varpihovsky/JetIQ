package com.varpihovsky.ui_data.state.profile

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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import com.google.accompanist.swiperefresh.SwipeRefreshState

actual class ProfileInteractionState(
    actual val scrollState: ScrollState,
    val refreshState: SwipeRefreshState,
    actual val onRefresh: () -> Unit,
    actual val onSettingsClick: () -> Unit,
    actual val paddingValues: PaddingValues
)