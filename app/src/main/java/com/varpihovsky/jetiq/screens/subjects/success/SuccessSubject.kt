package com.varpihovsky.jetiq.screens.subjects.success

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.varpihovsky.jetiq.ui.compose.BackIconButton
import com.varpihovsky.jetiq.ui.compose.ExpandableList
import com.varpihovsky.jetiq.ui.compose.SubjectInfo
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.SubjectTaskDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks

@ExperimentalAnimationApi
@Composable
fun SuccessSubjectScreen(markbookSubjectViewModel: SuccessSubjectViewModel) {
    val subjectDetails =
        markbookSubjectViewModel.subjectDetails.value.collectAsState(
            initial = SubjectDetailsWithTasks(
                subjectTasks = listOf()
            )
        ).value

    val subject =
        markbookSubjectViewModel.subject.value.collectAsState(initial = SubjectDTO()).value

    markbookSubjectViewModel.assignAppbar(
        title = subject.subject,
        icon = { BackIconButton(markbookSubjectViewModel::onBackNavButtonClick) }
    )

    BackHandler(onBack = markbookSubjectViewModel::onBackNavButtonClick)

    MarkbookSubjectScreen(
        subjectDetailsWithTasks = subjectDetails,
        subject = subject
    )
}

@ExperimentalAnimationApi
@Composable
private fun MarkbookSubjectScreen(
    subjectDetailsWithTasks: SubjectDetailsWithTasks,
    subject: SubjectDTO
) {
    val firstModuleTasks = subjectDetailsWithTasks.subjectTasks.filter { it.num_mod == "1" }
    val secondModuleTasks = subjectDetailsWithTasks.subjectTasks.filter { it.num_mod == "2" }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 7.dp)
        ) {
            Column {
                TaskList(
                    title = "Модуль 1",
                    tasks = firstModuleTasks,
                    sum = subjectDetailsWithTasks.subjectDetailsDTO.sum1.toString(),
                    mark = subjectDetailsWithTasks.subjectDetailsDTO.mark1.toString()
                )
                Divider(Modifier.fillMaxWidth())
                TaskList(
                    title = "Модуль 2",
                    tasks = secondModuleTasks,
                    sum = subjectDetailsWithTasks.subjectDetailsDTO.sum2.toString(),
                    mark = subjectDetailsWithTasks.subjectDetailsDTO.mark2.toString()
                )
            }
        }
        SubjectInfo(bigText = "Викладач", smallText = subject.t_name)
        SubjectInfo(
            bigText = "Всього",
            smallText = subjectDetailsWithTasks.subjectDetailsDTO.total.toString()
        )
        SubjectInfo(
            bigText = "Оцінка(ects)",
            smallText = subjectDetailsWithTasks.subjectDetailsDTO.ects
        )
        SubjectInfo(bigText = "Форма", smallText = subject.f_control)
    }
}

@ExperimentalAnimationApi
@Composable
fun TaskList(
    title: String,
    tasks: List<SubjectTaskDTO>,
    sum: String,
    mark: String
) {
    ExpandableList(
        modifier = Modifier.background(color = MaterialTheme.colors.surface),
        title = title
    ) {
        Column {
            tasks.forEach {
                Task(
                    modifier = Modifier.padding(vertical = 7.dp),
                    legend = it.legend,
                    points = it.points.toString()
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
            Task(
                modifier = Modifier.padding(vertical = 7.dp),
                legend = "Сума балів",
                points = sum
            )
            Divider(modifier = Modifier.fillMaxWidth())
            Task(
                modifier = Modifier.padding(vertical = 7.dp),
                legend = "Оцінка в 5-бальній системі",
                points = mark
            )
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun Task(modifier: Modifier = Modifier, legend: String, points: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp, end = 5.dp)
                .weight(3f),
            text = legend,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
        )
        Text(
            modifier = Modifier
                .padding(start = 5.dp, end = 10.dp)
                .weight(1f, true),
            text = points,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}