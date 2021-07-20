package com.varpihovsky.jetiq.screens.profile

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
fun Profile(
    profileViewModel: ProfileViewModel
) {
    val scrollState = profileViewModel.scrollState

    val profileState by profileViewModel.profile.collectAsState(UIProfileDTO())

    val successMarksInfoState by profileViewModel.successMarksInfo.collectAsState(listOf())
    val successSubjectsState by profileViewModel.successSubjects.collectAsState(listOf())

    val markbookInfo by profileViewModel.markbookMarksInfo.collectAsState(listOf())
    val markbookSubjects by profileViewModel.markbookSubjects.collectAsState(listOf())

    MapLifecycle(viewModel = profileViewModel)

    BackHandler(true, onBack = profileViewModel::onBackNavButtonClick)

    profileViewModel.emptyAppbar()

    Profile(
        profile = profileState,
        scrollState = scrollState,
        successMarksInfo = successMarksInfoState,
        subjects = successSubjectsState,
        markbookInfo = markbookInfo,
        markbookSubjects = markbookSubjects,
        successChecked = profileViewModel.data.successChecked.value,
        onSuccessToggle = profileViewModel::onSuccessToggle,
        markbookChecked = profileViewModel.data.markbookChecked.value,
        onMarkbookToggle = profileViewModel::onMarkbookToggle,
        refreshState = rememberSwipeRefreshState(isRefreshing = profileViewModel.isLoading.value),
        onRefresh = profileViewModel::onRefresh,
        onSettingsClick = profileViewModel::onSettingsClick
    )
}

@ExperimentalAnimationApi
@Composable
fun Profile(
    profile: UIProfileDTO,
    scrollState: ScrollState,
    successMarksInfo: List<MarksInfo>,
    subjects: List<UISubjectDTO>,
    markbookInfo: List<MarksInfo>,
    markbookSubjects: List<UISubjectDTO>,
    successChecked: Boolean,
    onSuccessToggle: (Boolean) -> Unit,
    markbookChecked: Boolean,
    onMarkbookToggle: (Boolean) -> Unit,
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
                    subjects = subjects,
                    checked = successChecked
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
                    marksInfo = markbookInfo
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                SubjectList(subjects = markbookSubjects)
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
    onToggle: (Boolean) -> Unit
) {
    InfoList(
        modifier = modifier,
        title = "Успішність:", info = { MarksList(marks = successMarksInfo) },
        moreInfoTitle = "Більше...",
        moreInfoContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                SubjectList(subjects = subjects)
            }
        }, checked = checked, onToggle = onToggle
    )
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
