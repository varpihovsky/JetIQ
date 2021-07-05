package com.varpihovsky.jetiq.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.varpihovsky.jetiq.ui.dto.MarksInfo
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO

@Composable
fun ProfileName(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 9.dp),
        text = text,
        style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Start)
    )
}

@Composable
fun Avatar(modifier: Modifier = Modifier, url: String) {
    Image(
        modifier = modifier,
        painter = rememberCoilPainter(
            request = url,
            fadeIn = true,
            requestBuilder = { transformations(CircleCropTransformation()) }),
        contentDescription = null
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
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Avatar(
            modifier = Modifier
                .requiredSize(35.dp)
                .weight(1f)
                .padding(start = 5.dp), url = profile.photoURL
        )
        ProfileName(
            modifier = Modifier.weight(6f),
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