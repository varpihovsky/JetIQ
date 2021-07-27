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

import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.dataTransfer.ViewModelData
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.repo_data.SubjectDTO
import com.varpihovsky.repo_data.relations.SubjectDetailsWithTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuccessSubjectViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val viewModelDataTransferManager: ViewModelDataTransferManager,
    private val subjectRepo: SubjectRepo,
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    private val _subjectDetails = mutableStateOf(emptyFlow<SubjectDetailsWithTasks>())
    val subjectDetails: State<Flow<SubjectDetailsWithTasks>> = _subjectDetails

    private val _subject = mutableStateOf(emptyFlow<SubjectDTO>())
    val subject: State<Flow<SubjectDTO>> = _subject

    init {
        viewModelScope.launch(dispatchers.Default) { collectTransferredData() }
    }

    private suspend fun collectTransferredData() {
        viewModelDataTransferManager.getFlowByTag(DATA_TRANSFER_TAG).collect { viewModelData ->
            (viewModelData as? ViewModelData<Int>)?.let {
                _subjectDetails.value = subjectRepo.getDetailsById(it.data)
                _subject.value = subjectRepo.getSubjectById(it.data)
            }
        }
    }

    companion object {
        const val DATA_TRANSFER_TAG = "success_subject_view_model"
    }
}