package com.varpihovsky.jetiq.ui.compose

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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.transform.CircleCropTransformation
import coil.transform.Transformation
import com.google.accompanist.coil.rememberCoilPainter
import com.varpihovsky.jetiq.R
import com.varpihovsky.ui_data.MarksInfo
import com.varpihovsky.ui_data.UIProfileDTO
import com.varpihovsky.ui_data.UISubjectDTO
import soup.compose.material.motion.Axis
import soup.compose.material.motion.MaterialSharedAxis

private const val DEFAULT_DRAG_VELOCITY = 100

@Composable
fun ProfileName(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 9.dp),
        text = text,
        style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Start)
    )
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    url: String,
    colorFilter: ColorFilter? = null,
    transformation: Transformation? = CircleCropTransformation(),
    placeholderEnabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        modifier = modifier,
        painter = rememberCoilPainter(
            request = url,
            fadeIn = true,
            requestBuilder = {
                transformation?.let { transformations(it) }
                if (placeholderEnabled) {
                    placeholder(R.drawable.ic_baseline_person_24)
                    error(R.drawable.ic_baseline_person_24)
                }
                this
            },
            previewPlaceholder = R.drawable.ic_baseline_person_24
        ),
        contentDescription = null,
        colorFilter = colorFilter,
        contentScale = contentScale
    )
}

@Composable
fun StudentInfo(
    profile: UIProfileDTO
) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        BoxInfo(bigText = profile.faculty, smallText = "Факультет")
        BoxInfo(bigText = profile.course.toString(), smallText = "Курс")
        BoxInfo(bigText = profile.groupName, smallText = "Група")
        BoxInfo(bigText = profile.subgroupNumber.toString(), smallText = "Номер підгрупи")
    }
}

@Composable
fun ProfileInfoBar(
    modifier: Modifier = Modifier,
    profile: UIProfileDTO
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Avatar(
            modifier = Modifier
                .padding(horizontal = 9.dp)
                .requiredSize(35.dp),
            url = profile.photoURL
        )
        ProfileName(
            text = profile.name
        )
    }
}

@Composable
fun ProfileSettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "settings",
            tint = color
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun MarksList(
    marks: List<MarksInfo>
) {
    Column {
        for (mark in marks) {
            BoxInfo(
                bigText = mark.grade.toString(),
                smallText = "${mark.semester} Семестр"
            )
        }
    }
}

@Composable
fun SubjectListPart(subjects: List<UISubjectDTO>) {
    val maxSemester = rememberMaxSemester(subjects)
    val semesterState = rememberSaveable { mutableStateOf(1) }
    val listToShow =
        remember { mutableStateOf(filterBySemester(subjects, semesterState.value)) }
    val forward = rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = rememberDraggableState {},
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    if (velocity > DEFAULT_DRAG_VELOCITY && semesterState.value != 1) {
                        semesterState.value--
                        listToShow.value = filterBySemester(subjects, semesterState.value)
                        forward.value = false
                    } else if (velocity < -DEFAULT_DRAG_VELOCITY && semesterState.value < maxSemester) {
                        semesterState.value++
                        listToShow.value = filterBySemester(subjects, semesterState.value)
                        forward.value = true
                    }
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(icon = Icons.Default.KeyboardArrowLeft) {
                if (semesterState.value != 1) {
                    semesterState.value--
                    listToShow.value = filterBySemester(subjects, semesterState.value)
                    forward.value = false
                }
            }
            Text(
                text = "Семестр ${semesterState.value}",
                style = MaterialTheme.typography.h5
            )
            IconButton(icon = Icons.Default.KeyboardArrowRight) {
                if (semesterState.value < maxSemester) {
                    semesterState.value++
                    listToShow.value = filterBySemester(subjects, semesterState.value)
                    forward.value = true
                }
            }
        }
        MaterialSharedAxis(
            targetState = semesterState.value,
            axis = Axis.X,
            forward = forward.value
        ) {
            Column {
                for (subject in listToShow.value) {
                    Subject(uiSubjectDTO = subject)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberMaxSemester(subjects: List<UISubjectDTO>) = remember {
    var counter = 0
    subjects.forEach { if (it.semester > counter) counter++ }
    counter
}

private fun filterBySemester(subjects: List<UISubjectDTO>, semester: Int) =
    subjects.filter { it.semester == semester }

@Composable
fun SubjectList(
    subjects: List<UISubjectDTO>
) {
    var currentSemester = 0
    for (subject in subjects) {
        if (currentSemester != subject.semester) {
            CenterLayoutItem {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "Семестр ${subject.semester}",
                    style = MaterialTheme.typography.h5
                )
                currentSemester = subject.semester
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            elevation = 1.dp
        ) {
            Subject(uiSubjectDTO = subject)
        }
    }
}

@Composable
fun Subject(uiSubjectDTO: UISubjectDTO) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(4f)) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .fillMaxWidth(),
                    text = uiSubjectDTO.subjectName,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .fillMaxWidth(),
                    text = uiSubjectDTO.teacherName,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start
                )
            }
            BoxInfo(
                modifier = Modifier.weight(1f),
                bigText = uiSubjectDTO.mark.toString(),
                smallText = "Оцінка"
            )
        }
    }
}