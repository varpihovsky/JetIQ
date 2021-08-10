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
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.entities.*
import com.varpihovsky.core_ui.compose.foundation.CenterLayoutItem
import com.varpihovsky.core_ui.compose.widgets.Avatar
import com.varpihovsky.core_ui.compose.widgets.InfoCard
import com.varpihovsky.core_ui.compose.widgets.InfoList
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.dto.UIProfileDTO
import com.varpihovsky.ui_data.dto.UISubjectDTO
import com.varpihovsky.ui_data.state.profile.ProfileInteractionState
import com.varpihovsky.ui_data.state.profile.ProfileState
import com.varpihovsky.ui_data.state.profile.SubjectListState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val AVATAR_SIZE = 200
const val SCROLL_OFFSET = -177

@Composable
expect fun Profile(profileViewModel: ProfileViewModel, paddingValues: PaddingValues)

@Composable
internal expect fun Profile(profileState: ProfileState)

@ExperimentalAnimationApi
@Composable
internal fun ProfileWall(profileState: ProfileState, onProfileTextPositioned: (Float) -> Unit) {
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
                interactionState = profileState.interactionState
            )
        }

        InfoCard {
            ProfileSubjectsList(
                state = profileState.markbookState,
                interactionState = profileState.interactionState
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
        // Blurred background
        //
//        Avatar(
//            modifier = Modifier
//                .fillMaxSize()
//                .zIndex(-2f)
//                .background(MaterialTheme.colors.background),
//            url = profile.photoURL,
//            transformation = null,
//            placeholderEnabled = false,
//            contentScale = ContentScale.Crop,
//        )
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

@ExperimentalAnimationApi
@Composable
internal fun ProfileSubjectsList(
    modifier: Modifier = Modifier,
    state: SubjectListState,
    interactionState: ProfileInteractionState
) {
    var position by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    InfoList(
        modifier = modifier.onGloballyPositioned {
            position = interactionState.scrollState.value +
                    it.positionInRoot().y + SCROLL_OFFSET
        },
        title = state.title, info = { MarksList(marks = state.marksInfo) },
        moreInfoTitle = "Більше...",
        checked = state.checked,
        onToggle = {
            state.onToggle(it)
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
                    listType = state.listType,
                    list = state.subjects,
                    onClick = state.onSubjectClick,
                    key = state.title
                )
            }
        }
    )
}

@ExperimentalAnimationApi
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
