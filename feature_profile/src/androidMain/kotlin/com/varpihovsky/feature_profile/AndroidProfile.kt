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
package com.varpihovsky.feature_profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.varpihovsky.core_lifecycle.assignAppbar
import com.varpihovsky.core_lifecycle.emptyAppbar
import com.varpihovsky.core_ui.compose.foundation.VerticalScrollLayout
import com.varpihovsky.core_ui.compose.widgets.SettingsIconButton
import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.state.profile.ProfileAppbarState
import com.varpihovsky.ui_data.state.profile.ProfileInteractionState
import com.varpihovsky.ui_data.state.profile.ProfileState
import com.varpihovsky.ui_data.state.profile.SubjectListState

@ExperimentalAnimationApi
@Composable
actual fun Profile(profileViewModel: ProfileViewModel, paddingValues: PaddingValues) {
    val buttonLocation by profileViewModel.expandButtonLocation.collectAsState(initial = ExpandButtonLocation.LOWER)
    val profile = profileViewModel.profile.collectAsState().value

    Profile(
        profileState = ProfileState(
            profile = profile,
            interactionState = ProfileInteractionState(
                scrollState = profileViewModel.scrollState,
                refreshState = rememberSwipeRefreshState(isRefreshing = profileViewModel.isLoading.value),
                onRefresh = profileViewModel::onRefresh,
                onSettingsClick = profileViewModel::onSettingsClick,
                paddingValues = paddingValues
            ),
            appbarState = ProfileAppbarState(
                onShow = {
                    profileViewModel.assignAppbar(
                        title = profile.name,
                        actions = { SettingsIconButton(onClick = profileViewModel::onSettingsClick) }
                    )
                },
                onHide = { profileViewModel.emptyAppbar() }
            ),
            successState = SubjectListState(
                title = "Успішність:",
                marksInfo = profileViewModel.successMarksInfo.collectAsState(listOf()).value,
                subjects = profileViewModel.successSubjects.collectAsState().value,
                listType = profileViewModel.successListType.collectAsState(initial = SubjectListType.PARTIAL).value,
                checked = profileViewModel.data.successChecked.value,
                onToggle = profileViewModel::onSuccessToggle,
                onSubjectClick = profileViewModel::onSubjectClick,
                buttonLocation = buttonLocation
            ),
            markbookState = SubjectListState(
                title = "Залікова книжка:",
                marksInfo = profileViewModel.markbookMarksInfo.collectAsState(listOf()).value,
                subjects = profileViewModel.markbookSubjects.collectAsState().value,
                listType = profileViewModel.markbookListType.collectAsState(initial = SubjectListType.PARTIAL).value,
                checked = profileViewModel.data.markbookChecked.value,
                onToggle = profileViewModel::onMarkbookToggle,
                onSubjectClick = profileViewModel::onMarkbookClick,
                buttonLocation = buttonLocation
            )
        )
    )
}

@ExperimentalAnimationApi
@Composable
internal actual fun Profile(profileState: ProfileState) {
    val profileTextPosition = remember { mutableStateOf(0f) }

    val profileTextShown = profileTextPosition.value < 0

    LaunchedEffect(key1 = profileTextShown, key2 = remember { Any() }) {
        if (profileTextShown) profileState.appbarState.onShow() else profileState.appbarState.onHide()
    }

    SwipeRefresh(
        state = profileState.interactionState.refreshState,
        onRefresh = profileState.interactionState.onRefresh,
        indicatorPadding = PaddingValues(top = profileState.interactionState.paddingValues.calculateTopPadding())
    ) {
        Box {
            if (!profileTextShown) {
                SettingsIconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(2f),
                    onClick = profileState.interactionState.onSettingsClick,
                )
            }
            VerticalScrollLayout(scrollState = profileState.interactionState.scrollState) {
                ProfileWall(profileState = profileState) {
                    profileTextPosition.value = it
                }
            }
        }
    }
}
