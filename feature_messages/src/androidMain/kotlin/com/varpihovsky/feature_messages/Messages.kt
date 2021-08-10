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
package com.varpihovsky.feature_messages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.varpihovsky.ui_data.dto.UIMessageDTO

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
actual fun MessagesScreen(
    messages: List<UIMessageDTO>,
    onClick: () -> Unit,
    loadingState: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(state = SwipeRefreshState(loadingState), onRefresh = onRefresh) {
        MessagesList(messages = messages)
        NewMessageButton(onClick = onClick)
    }

}