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
package com.varpihovsky.feature_profile.profile

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.dto.MarksInfo
import com.varpihovsky.ui_data.dto.UISubjectDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class SubjectsComponent(
    jetIQComponentContext: JetIQComponentContext,
    val marksInfo: Flow<List<MarksInfo>>,
    val state: StateFlow<List<UISubjectDTO>>,
    val listType: Flow<SubjectListType>,
    private val onSubjectClickCallback: (UISubjectDTO) -> Unit
) : JetIQComponentContext by jetIQComponentContext {
    val isChecked: Value<Boolean> by lazy { _isChecked }

    private val _isChecked = MutableValue(false)

    fun onToggle() {
        _isChecked.value = !_isChecked.value
    }

    fun onSubjectClick(uiSubjectDTO: UISubjectDTO) {
        onSubjectClickCallback(uiSubjectDTO)
    }
}