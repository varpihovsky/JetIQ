package com.varpihovsky.jetiq.screens.profile

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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.transform.BlurTransformation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.varpihovsky.jetiq.ui.compose.*
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.MarksInfo
import com.varpihovsky.ui_data.UIProfileDTO
import com.varpihovsky.ui_data.UISubjectDTO
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val SCROLL_DP = 503f
const val AVATAR_SIZE = 200
const val SCROLL_OFFSET = -177

@ExperimentalAnimationApi
@Composable
fun Profile(profileViewModel: ProfileViewModel) {
    val scrollState = profileViewModel.scrollState

    val profileState by profileViewModel.profile.collectAsState(UIProfileDTO())

    val successMarksInfoState by profileViewModel.successMarksInfo.collectAsState(listOf())
    val successSubjectsState by profileViewModel.successSubjects.collectAsState(listOf())
    val successListType by profileViewModel.successListType.collectAsState(initial = SubjectListType.PARTIAL)

    val markbookInfo by profileViewModel.markbookMarksInfo.collectAsState(listOf())
    val markbookSubjects by profileViewModel.markbookSubjects.collectAsState(listOf())
    val markbookListType by profileViewModel.markbookListType.collectAsState(initial = SubjectListType.PARTIAL)

    MapLifecycle(viewModel = profileViewModel)

    BackHandler(true, onBack = profileViewModel::onBackNavButtonClick)

    profileViewModel.emptyAppbar()

    // collectAsState provides empty value on first composition and than recompose with right one
    // That's producing bug that breaks lists visibility animation and current list semester state.
    // This is pretty bad fix of this bug. The better one is to make implementation of produceState
    // which saves state via rememberSavable command.
    if (markbookSubjects.isEmpty() && !profileViewModel.isLoading.value) {
        return
    }

    Profile(
        profile = profileState,
        scrollState = scrollState,
        successMarksInfo = successMarksInfoState,
        successSubjects = successSubjectsState,
        successListType = successListType,
        markbookInfo = markbookInfo,
        markbookSubjects = markbookSubjects,
        markbookListType = markbookListType,
        successChecked = profileViewModel.data.successChecked.value,
        onSuccessToggle = profileViewModel::onSuccessToggle,
        markbookChecked = profileViewModel.data.markbookChecked.value,
        onMarkbookToggle = profileViewModel::onMarkbookToggle,
        refreshState = rememberSwipeRefreshState(isRefreshing = profileViewModel.isLoading.value),
        onRefresh = profileViewModel::onRefresh,
        onSettingsClick = profileViewModel::onSettingsClick
    )
}

//TODO: Move params into class.
@ExperimentalAnimationApi
@Composable
fun Profile(
    profile: UIProfileDTO,
    scrollState: ScrollState,
    markbookChecked: Boolean,
    markbookInfo: List<MarksInfo>,
    markbookSubjects: List<UISubjectDTO>,
    onMarkbookToggle: (Boolean) -> Unit,
    markbookListType: SubjectListType,
    successChecked: Boolean,
    onSuccessToggle: (Boolean) -> Unit,
    successMarksInfo: List<MarksInfo>,
    successSubjects: List<UISubjectDTO>,
    successListType: SubjectListType,
    refreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val a = if (scrollState.value > SCROLL_DP) SCROLL_DP else scrollState.value.toFloat()
    val heightState = (((a / SCROLL_DP)) * 180f).dp
    val profileTextShown = heightState == 180.dp

    ProfileAppBar(
        profile = profile,
        heightState = heightState,
        profileTextShown = profileTextShown,
        onSettingsClick = onSettingsClick
    )

    SwipeRefresh(state = refreshState, onRefresh = onRefresh) {
        VerticalScrollLayout(
            modifier = Modifier.zIndex(-1f),
            scrollState = scrollState
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            CenterLayoutItem {
                ProfileName(text = profile.name)
            }

            Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
                StudentInfo(profile = profile)
            }

            InfoCard {
                var successPosition by remember { mutableStateOf(0f) }

                Success(
                    modifier = Modifier.onGloballyPositioned {
                        successPosition = scrollState.value + it.positionInRoot().y + SCROLL_OFFSET
                    },
                    successMarksInfo = successMarksInfo,
                    subjects = successSubjects,
                    checked = successChecked,
                    listType = successListType
                ) {
                    onSuccessToggle(it)
                    if (!it) {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(successPosition.roundToInt())
                        }
                    }
                }
            }

            InfoCard {
                var markbookPosition by remember { mutableStateOf(0f) }

                Markbook(
                    modifier = Modifier.onGloballyPositioned {
                        markbookPosition = scrollState.value + it.positionInRoot().y + SCROLL_OFFSET
                    },
                    checked = markbookChecked,
                    markbookSubjects = markbookSubjects,
                    marksInfo = markbookInfo,
                    listType = markbookListType
                ) {
                    onMarkbookToggle(it)
                    if (!it) {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(markbookPosition.roundToInt())
                        }
                    }
                }
            }
        }

    }
}

@ExperimentalAnimationApi
@Composable
fun Markbook(
    modifier: Modifier = Modifier,
    markbookSubjects: List<UISubjectDTO>,
    marksInfo: List<MarksInfo>,
    checked: Boolean,
    listType: SubjectListType,
    onToggle: (Boolean) -> Unit
) {
    InfoList(
        modifier = modifier,
        title = "Залікова книжка:",
        info = { MarksList(marks = marksInfo) },
        moreInfoTitle = "Більше...",
        checked = checked,
        onToggle = onToggle,
        moreInfoContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                ShowListByType(listType = listType, list = markbookSubjects)
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun Success(
    modifier: Modifier = Modifier,
    successMarksInfo: List<MarksInfo>,
    subjects: List<UISubjectDTO>,
    checked: Boolean,
    listType: SubjectListType,
    onToggle: (Boolean) -> Unit
) {
    InfoList(
        modifier = modifier,
        title = "Успішність:", info = { MarksList(marks = successMarksInfo) },
        moreInfoTitle = "Більше...",
        checked = checked,
        onToggle = onToggle,
        moreInfoContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                ShowListByType(listType = listType, list = subjects)
            }
        }
    )
}

@Composable
private fun ShowListByType(listType: SubjectListType, list: List<UISubjectDTO>) {
    when (listType) {
        SubjectListType.FULL -> SubjectList(subjects = list)
        SubjectListType.PARTIAL -> SubjectListPart(subjects = list)
    }
}

@Composable
fun ProfileAppBar(
    profile: UIProfileDTO,
    heightState: Dp,
    profileTextShown: Boolean,
    onSettingsClick: () -> Unit
) {
    val elevation = if (profileTextShown) 10.dp else 0.dp
    val zIndex = if (heightState == 180.dp) 10f else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(15f)
    ) {
        ProfileSettingsButton(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onClick = onSettingsClick,
            color = MaterialTheme.colors.onSurface
        )
    }

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .offset(y = -heightState)
            .zIndex(zIndex),
        elevation = elevation,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (profileTextShown) {
                ProfileInfoBar(modifier = Modifier.align(Alignment.BottomCenter), profile = profile)
            } else {
                Avatar(
                    modifier = Modifier
                        .requiredSize(AVATAR_SIZE.dp)
                        .padding(5.dp)
                        .align(Alignment.Center)
                        .padding(bottom = 30.dp)
                        .zIndex(zIndex)
                        .shadow(elevation = 10.dp, shape = CircleShape),
                    url = profile.photoURL
                )
                Avatar(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(zIndex - 2f)
                        .background(MaterialTheme.colors.background),
                    url = profile.photoURL,
                    transformation = BlurTransformation(LocalContext.current),
                    placeholderEnabled = false,
                    contentScale = ContentScale.Crop
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(zIndex - 1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0f to MaterialTheme.colors.background.copy(alpha = 0.5f),
                                    0.7f to MaterialTheme.colors.background.copy(alpha = 0.75f),
                                    1f to MaterialTheme.colors.background
                                )
                            )
                        )
                )
            }
        }

    }
}
