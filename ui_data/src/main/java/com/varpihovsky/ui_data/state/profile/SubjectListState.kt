package com.varpihovsky.ui_data.state.profile

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

import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.dto.MarksInfo
import com.varpihovsky.ui_data.dto.UISubjectDTO

data class SubjectListState(
    val title: String,
    val marksInfo: List<MarksInfo>,
    val subjects: List<UISubjectDTO>,
    val listType: SubjectListType,
    val checked: Boolean,
    val onToggle: (Boolean) -> Unit,
    val onSubjectClick: (UISubjectDTO) -> Unit,
    val buttonLocation: ExpandButtonLocation
)