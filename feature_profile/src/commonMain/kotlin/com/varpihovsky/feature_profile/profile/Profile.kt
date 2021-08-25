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
package com.varpihovsky.feature_profile.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.core_ui.compose.entities.*
import com.varpihovsky.core_ui.compose.foundation.CenterLayoutItem
import com.varpihovsky.core_ui.compose.foundation.SwipeRefresh
import com.varpihovsky.core_ui.compose.foundation.VerticalScrollLayout
import com.varpihovsky.core_ui.compose.widgets.Avatar
import com.varpihovsky.core_ui.compose.widgets.InfoCard
import com.varpihovsky.core_ui.compose.widgets.InfoList
import com.varpihovsky.core_ui.compose.widgets.SettingsIconButton
import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.dto.UIProfileDTO
import com.varpihovsky.ui_data.dto.UISubjectDTO
import com.varpihovsky.ui_data.state.profile.ProfileAppbarState
import com.varpihovsky.ui_data.state.profile.ProfileInteractionState
import com.varpihovsky.ui_data.state.profile.ProfileState
import com.varpihovsky.ui_data.state.profile.SubjectListState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val AVATAR_SIZE = 200
const val SCROLL_OFFSET = -177

@Composable
internal fun Profile(profileComponent: ProfileComponent) {
    val buttonLocation by profileComponent.expandButtonLocation.collectAsState(initial = ExpandButtonLocation.LOWER)
    val profile = profileComponent.profile.collectAsState().value

    Profile(
        profileComponent = profileComponent,
        profileState = ProfileState(
            profile = profile,
            interactionState = ProfileInteractionState(
                scrollState = profileComponent.scrollState,
                isRefreshing = profileComponent.isLoading.value,
                onRefresh = profileComponent::onRefresh,
                onSettingsClick = profileComponent::onSettingsClick,
                paddingValues = LocalCompositionState.current.paddingValues
            ),
            appbarState = ProfileAppbarState(
                onShow = {
                    profileComponent.appBarController.run {
                        show()
                        when (LocalCompositionState.current.currentMode) {
                            Mode.Mobile -> {
                                setText(profile.name)
                                hideIcon()
                                setActions { SettingsIconButton(onClick = profileComponent::onSettingsClick) }
                            }
                            Mode.Desktop -> {
                                setText("Профіль")
                                setIconToDrawer()
                                setActions { }
                            }
                        }
                    }
                },
                onHide = {
                    when (LocalCompositionState.current.currentMode) {
                        Mode.Mobile -> profileComponent.appBarController.hide()
                        Mode.Desktop -> profileComponent.appBarController.run {
                            show()
                            setText("Профіль")
                            setIconToDrawer()
                            setActions { }
                        }
                    }
                }
            ),
            successState = SubjectListState(
                title = "Успішність:",
                buttonLocation = buttonLocation
            ),
            markbookState = SubjectListState(
                title = "Залікова книжка:",
                buttonLocation = buttonLocation
            )
        )
    )
}

@Composable
internal fun Profile(profileState: ProfileState, profileComponent: ProfileComponent) {
    val profileTextPosition = remember { mutableStateOf(0f) }

    val profileTextShown = profileTextPosition.value < 0

    if (profileTextShown) profileState.appbarState.onShow() else profileState.appbarState.onHide()

    SwipeRefresh(
        modifier = Modifier.padding(
            if (LocalCompositionState.current.currentMode is Mode.Desktop) LocalCompositionState.current.paddingValues
            else PaddingValues()
        ),
        isRefreshing = profileState.interactionState.isRefreshing,
        onRefresh = profileState.interactionState.onRefresh,
        indicatorPadding = PaddingValues(top = profileState.interactionState.paddingValues.calculateTopPadding())
    ) {
        Box {
            if (!profileTextShown && LocalCompositionState.current.currentMode is Mode.Mobile) {
                SettingsIconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .zIndex(2f),
                    onClick = profileState.interactionState.onSettingsClick,
                )
            }
            VerticalScrollLayout(scrollState = profileState.interactionState.scrollState) {
                ProfileWall(
                    profileState = profileState,
                    profileComponent.successSubjectsComponent,
                    profileComponent.markbookComponent
                ) {
                    profileTextPosition.value = it
                }
            }
        }
    }
}

@Composable
internal fun ProfileWall(
    profileState: ProfileState,
    successSubjectsComponent: SubjectsComponent,
    markbookSubjectsComponent: SubjectsComponent,
    onProfileTextPositioned: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = profileState.interactionState.paddingValues.calculateBottomPadding())
    ) {
        ProfileInfo(
            profile = profileState.profile,
            onProfileTextPositioned = onProfileTextPositioned
        )

        InfoCard {
            ProfileSubjectsList(
                state = profileState.successState,
                interactionState = profileState.interactionState,
                subjectsComponent = successSubjectsComponent
            )
        }

        InfoCard {
            ProfileSubjectsList(
                state = profileState.markbookState,
                interactionState = profileState.interactionState,
                subjectsComponent = markbookSubjectsComponent
            )
        }
    }
}

@Composable
internal fun ProfileInfo(
    profile: UIProfileDTO,
    onProfileTextPositioned: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(bottom = 10.dp)
    ) {
        Avatar(
            modifier = Modifier
                .requiredSize(AVATAR_SIZE.dp)
                .padding(5.dp)
                .align(Alignment.Center),
            url = profile.photoURL,
        )
    }

    CenterLayoutItem(modifier = Modifier.onGloballyPositioned { onProfileTextPositioned(it.positionInRoot().y) }) {
        ProfileName(
            text = profile.name
        )
    }

    Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
        StudentInfo(profile = profile)
    }
}

@Composable
internal fun ProfileSubjectsList(
    modifier: Modifier = Modifier,
    state: SubjectListState,
    subjectsComponent: SubjectsComponent,
    interactionState: ProfileInteractionState
) {
    var position by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    val isChecked by subjectsComponent.isChecked.subscribeAsState()
    val marksInfo by subjectsComponent.marksInfo.collectAsState(listOf())
    val subjects by subjectsComponent.state.collectAsState()
    val listType by subjectsComponent.listType.collectAsState(SubjectListType.PARTIAL)

    InfoList(
        modifier = modifier.onGloballyPositioned {
            position = interactionState.scrollState.value +
                    it.positionInRoot().y + SCROLL_OFFSET
        },
        title = state.title, info = { MarksList(marks = marksInfo) },
        moreInfoTitle = "Більше...",
        checked = isChecked,
        onToggle = {
            subjectsComponent.onToggle()
            if (!it) {
                coroutineScope.launch {
                    interactionState.scrollState.animateScrollTo(position.roundToInt())
                }
            }
        },
        buttonLocation = state.buttonLocation,
        moreInfoContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                ShowListByType(
                    listType = listType,
                    list = subjects,
                    onClick = subjectsComponent::onSubjectClick,
                    key = state.title
                )
            }
        }
    )
}

@Composable
private fun ShowListByType(
    listType: SubjectListType,
    list: List<UISubjectDTO>,
    onClick: (UISubjectDTO) -> Unit,
    key: String
) {
    when (listType) {
        SubjectListType.FULL -> SubjectList(subjects = list, onClick = onClick)
        SubjectListType.PARTIAL -> SubjectListPart(
            subjects = list,
            onClick = onClick,
            semesterStateKey = key
        )
    }
}
