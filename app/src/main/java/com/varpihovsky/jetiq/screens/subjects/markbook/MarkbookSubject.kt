package com.varpihovsky.jetiq.screens.subjects.markbook

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
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.varpihovsky.jetiq.ui.compose.BackIconButton
import com.varpihovsky.jetiq.ui.compose.SubjectInfo
import com.varpihovsky.repo_data.MarkbookSubjectDTO

@Composable
fun MarkbookSubjectScreen(markbookSubjectViewModel: MarkbookSubjectViewModel) {
    val subject =
        markbookSubjectViewModel.subject.value.collectAsState(initial = MarkbookSubjectDTO()).value

    markbookSubjectViewModel.assignAppbar(
        title = subject.subj_name,
        icon = { BackIconButton(markbookSubjectViewModel::onBackNavButtonClick) }
    )

    BackHandler(onBack = markbookSubjectViewModel::onBackNavButtonClick)

    MarkbookSubjectScreen(
        markbookSubjectDTO = subject
    )
}

@Composable
private fun MarkbookSubjectScreen(markbookSubjectDTO: MarkbookSubjectDTO) {
    Column {
        SubjectInfo(bigText = "Викладач", smallText = markbookSubjectDTO.teacher)
        SubjectInfo(bigText = "Дата", smallText = markbookSubjectDTO.date)
        SubjectInfo(bigText = "Кредитів", smallText = markbookSubjectDTO.credits)
        SubjectInfo(bigText = "Всього", smallText = markbookSubjectDTO.total.toString())
        SubjectInfo(bigText = "Оцінка(5-бальна)", smallText = markbookSubjectDTO.mark)
        SubjectInfo(bigText = "Оцінка(ects)", smallText = markbookSubjectDTO.ects)
        SubjectInfo(bigText = "Форма", smallText = markbookSubjectDTO.form)
    }
}