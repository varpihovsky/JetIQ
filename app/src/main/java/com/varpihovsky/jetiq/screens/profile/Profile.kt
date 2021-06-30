package com.varpihovsky.jetiq.screens.profile

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.varpihovsky.jetiq.ui.compose.*
import com.varpihovsky.jetiq.ui.dto.MarksInfo
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO
import com.varpihovsky.jetiq.ui.theme.JetIQTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val exampleProfile = UIProfileDTO(
    0,
    "Подрезенко Владислав",
    "ФКСА",
    1,
    "1ІСТ-20б",
    2,
    "https://iq.vntu.edu.ua/b06175/getfile.php?stud_id=11411"
)

const val SCROLL_DP = 503f
const val AVATAR_SIZE = 200
const val SCROLL_OFFSET = -177

@ExperimentalAnimationApi
@Preview
@Composable
fun ProfilePreviewLight() {
    JetIQTheme(darkTheme = false) {
        ExampleProfile()
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun ProfilePreviewDark() {
    JetIQTheme(darkTheme = true) {
        ExampleProfile()
    }
}

@ExperimentalAnimationApi
@Composable
private fun ExampleProfile() {
    val successSubjectsList = mutableListOf<UISubjectDTO>()

    repeat(15) { i ->
        val semester = if (i < 7) 1 else 2
        successSubjectsList.add(
            UISubjectDTO(
                0,
                "Комп'ютерна графіка",
                "Софина Ольга Юріївна",
                100,
                semester
            )
        )
    }

    val markbookSubjectsList = mutableListOf<UISubjectDTO>()

    var semester = 1

    repeat(40) { i ->
        if (semester % 10 == 0) {
            semester++
        }

        markbookSubjectsList.add(
            UISubjectDTO(
                0,
                "Комп'ютерна графіка",
                "Софина Ольга Юріївна",
                100,
                semester
            )
        )
    }

    val markbookInfoList = listOf(
        MarksInfo(1, 100),
        MarksInfo(2, 100),
        MarksInfo(3, 100),
        MarksInfo(4, 100)
    )

    var successChecked by remember { mutableStateOf(false) }
    var markbookChecked by remember { mutableStateOf(false) }

    Profile(
        profile = exampleProfile,
        scrollState = rememberScrollState(),
        successMarksInfo = listOf(MarksInfo(1, 100), MarksInfo(2, 100)),
        subjects = successSubjectsList,
        markbookInfo = markbookInfoList,
        markbookSubjects = markbookSubjectsList,
        successChecked = successChecked,
        onSuccessToggle = { successChecked = !successChecked },
        markbookChecked = markbookChecked,
        onMarkbookToggle = { markbookChecked = !markbookChecked },
        refreshState = rememberSwipeRefreshState(isRefreshing = true),
        onRefresh = {},
        onSettingsClick = {}
    )
}

@ExperimentalAnimationApi
@Composable
fun Profile(
    profileViewModel: ProfileViewModel
) {
    val scrollState = profileViewModel.scrollState

    val profileState by profileViewModel.data.profile.observeAsState(UIProfileDTO())

    val successMarksInfoState by profileViewModel.data.successMarksInfo.observeAsState(listOf())
    val successSubjectsState by profileViewModel.data.successSubjects.observeAsState(listOf())

    val markbookInfo by profileViewModel.data.markbookMarksInfo.observeAsState(listOf())
    val markbookSubjects by profileViewModel.data.markbookSubjects.observeAsState(listOf())

    val successChecked by profileViewModel.data.successChecked.observeAsState(initial = false)
    val markbookChecked by profileViewModel.data.markbookChecked.observeAsState(initial = false)

    val isRefreshing by profileViewModel.isLoading.observeAsState(initial = false)
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    if (isRefreshing) {
        swipeRefreshState.isRefreshing = true
    }

    CollectExceptions(viewModel = profileViewModel)

    profileViewModel.emptyAppbar()

    Profile(
        profile = profileState,
        scrollState = scrollState,
        successMarksInfo = successMarksInfoState,
        subjects = successSubjectsState,
        markbookInfo = markbookInfo,
        markbookSubjects = markbookSubjects,
        successChecked = successChecked,
        onSuccessToggle = profileViewModel::onSuccessToggle,
        markbookChecked = markbookChecked,
        onMarkbookToggle = profileViewModel::onMarkbookToggle,
        refreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing),
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

    Log.d("App", scrollState.value.toString())
    Log.d("App", heightState.value.toString())

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

            ProfileInfoCard {
                var successPosition by remember { mutableStateOf(0f) }

                Success(
                    modifier = Modifier.onGloballyPositioned {
                        successPosition = scrollState.value + it.positionInRoot().y + SCROLL_OFFSET
                        Log.d("Application", "Success: $successPosition")
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

            ProfileInfoCard {
                var markbookPosition by remember { mutableStateOf(0f) }

                Markbook(
                    modifier = Modifier.onGloballyPositioned {
                        markbookPosition = scrollState.value + it.positionInRoot().y + SCROLL_OFFSET
                        Log.d("Application", "Markbook: $markbookPosition")
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
    val backgroundColor =
        if (heightState == 180.dp) MaterialTheme.colors.primary else Color.Transparent
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
                        .padding(bottom = 30.dp),
                    url = profile.photoURL
                )
            }
        }

    }
}

@Composable
private fun ProfileInfoCard(
    content: @Composable () -> Unit
) {
    InfoCard(
        modifier = Modifier.padding(vertical = 20.dp),
        cardModifier = Modifier.fillMaxWidth(0.92f)
    ) {
        content()
    }
}
