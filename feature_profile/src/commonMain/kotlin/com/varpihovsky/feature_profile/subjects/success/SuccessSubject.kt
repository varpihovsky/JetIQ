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
package com.varpihovsky.feature_profile.subjects.success

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_lifecycle.composition.LocalCompositionState
import com.varpihovsky.core_lifecycle.composition.Mode
import com.varpihovsky.core_ui.compose.entities.TaskList
import com.varpihovsky.core_ui.compose.widgets.SubjectInfo
import com.varpihovsky.jetiqApi.data.Subject
import com.varpihovsky.jetiqApi.data.SubjectDetails

@Composable
internal fun SuccessSubjectScreen(successSubjectComponent: SuccessSubjectComponent) {
    val subjectDetails by successSubjectComponent.subjectDetails.collectAsState(initial = null)
    val subject by successSubjectComponent.subject.collectAsState(initial = null)

    if (LocalCompositionState.current.currentMode is Mode.Mobile) {
        successSubjectComponent.appBarController.run {
            show()
            setText(subject?.subject ?: "")
            setIconToBack()
        }
        successSubjectComponent.bottomBarController.hide()
    }

    SuccessSubjectScreen(
        subjectDetailsWithTasks = subjectDetails,
        subject = subject
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SuccessSubjectScreen(
    subjectDetailsWithTasks: SubjectDetails?,
    subject: Subject?
) {
    val firstModuleTasks =
        subjectDetailsWithTasks?.tasks?.filter { it.module == "1" } ?: emptyList()
    val secondModuleTasks =
        subjectDetailsWithTasks?.tasks?.filter { it.module == "2" } ?: emptyList()

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
                    sum = subjectDetailsWithTasks?.hundredPointMarkFirstModule?.toString() ?: "...",
                    mark = subjectDetailsWithTasks?.fivePointMarkFirstModule?.toString() ?: "..."
                )
                Divider(Modifier.fillMaxWidth())
                TaskList(
                    title = "Модуль 2",
                    tasks = secondModuleTasks,
                    sum = subjectDetailsWithTasks?.hundredPointMarkSecondModule?.toString()
                        ?: "...",
                    mark = subjectDetailsWithTasks?.fivePointMarkSecondModule?.toString() ?: "..."
                )
            }
        }
        SubjectInfo(bigText = "Викладач", smallText = subject?.teacherName ?: "")
        SubjectInfo(
            bigText = "Всього",
            smallText = subjectDetailsWithTasks?.totalHundredPointMark?.toString() ?: "..."
        )
        SubjectInfo(
            bigText = "Оцінка(ects)",
            smallText = subjectDetailsWithTasks?.ectsMark ?: "..."
        )
        SubjectInfo(bigText = "Форма", smallText = subject?.controlForm ?: "...")
    }
}