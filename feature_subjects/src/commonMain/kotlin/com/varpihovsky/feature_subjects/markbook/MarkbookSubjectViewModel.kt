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
package com.varpihovsky.feature_subjects.markbook

import androidx.compose.runtime.State
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.dataTransfer.ViewModelData
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_lifecycle.mutableStateOf
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiqApi.data.MarkbookSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MarkbookSubjectViewModel(
    dispatchers: CoroutineDispatchers,
    private val viewModelDataTransferManager: ViewModelDataTransferManager,
    private val subjectRepo: SubjectRepo,
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager
) : JetIQViewModel(
    appbarManager,
    navigationController,
    exceptionEventManager
) {

    private val _subject = mutableStateOf(flow<MarkbookSubject?> { })
    val subject: State<Flow<MarkbookSubject?>> = _subject

    init {
        viewModelScope.launch(dispatchers.Default) { collectTransferredData() }
    }

    private suspend fun collectTransferredData() {
        viewModelDataTransferManager.getFlowByTag(DATA_TRANSFER_TAG).collect { viewModelData ->
            (viewModelData as? ViewModelData<Int>)?.let {
                _subject.value = subjectRepo.getMarkbookById(it.data)
            }
        }
    }

    companion object {
        const val DATA_TRANSFER_TAG = "markbook_subject_view_model"
    }
}
