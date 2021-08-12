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
package com.varpihovsky.feature_profile

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.coroutines.runBlocking
import com.varpihovsky.core.dataTransfer.ViewModelData
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_lifecycle.mutableStateOf
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.feature_subjects.markbook.MarkbookSubjectViewModel
import com.varpihovsky.feature_subjects.success.SuccessSubjectViewModel
import com.varpihovsky.ui_data.dto.UIProfileDTO
import com.varpihovsky.ui_data.dto.UISubjectDTO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val profileInteractor: ProfileInteractor,
    override val navigationController: NavigationController,
    viewModelDataTransferManager: ViewModelDataTransferManager,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
    userPreferencesRepo: UserPreferencesRepo
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager), Refreshable {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)
    override val isLoading: State<Boolean>
        get() = profileInteractor.isLoading

    lateinit var profile: StateFlow<UIProfileDTO>

    val successMarksInfo = profileInteractor.successData.map { it.first }.distinctUntilChanged()
    lateinit var successSubjects: StateFlow<List<UISubjectDTO>>

    val markbookMarksInfo = profileInteractor.markbookData.map { it.first }.distinctUntilChanged()
    lateinit var markbookSubjects: StateFlow<List<UISubjectDTO>>

    private val successChecked = mutableStateOf(false)
    private val markbookChecked = mutableStateOf(false)

    private val markbookViewModelFlow =
        viewModelDataTransferManager.getFlowByTag(MarkbookSubjectViewModel.DATA_TRANSFER_TAG)

    private val successViewModelFlow =
        viewModelDataTransferManager.getFlowByTag(SuccessSubjectViewModel.DATA_TRANSFER_TAG)

    val successListType = userPreferencesRepo.flow
        .map { it.successListType }
        .distinctUntilChanged()

    val markbookListType = userPreferencesRepo.flow
        .map { it.markbookListType }
        .distinctUntilChanged()

    val expandButtonLocation = userPreferencesRepo.flow
        .map { it.profileListExpandButtonLocation }
        .distinctUntilChanged()

    init {
        runBlocking {
            viewModelScope.launch {
                profile = profileInteractor.profileData.distinctUntilChanged().stateIn(
                    viewModelScope, SharingStarted.Eagerly, UIProfileDTO()
                )
                successSubjects =
                    profileInteractor.successData.map { it.second }.distinctUntilChanged()
                        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
                markbookSubjects =
                    profileInteractor.markbookData.map { it.second }.distinctUntilChanged()
                        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
            }.join()
        }
    }

    inner class Data {
        val successChecked: State<Boolean> = this@ProfileViewModel.successChecked
        val markbookChecked: State<Boolean> = this@ProfileViewModel.markbookChecked
    }

    fun onSuccessToggle(checked: Boolean) {
        successChecked.value = checked
    }

    fun onMarkbookToggle(checked: Boolean) {
        markbookChecked.value = checked
    }

    fun onMarkbookClick(uiSubjectDTO: UISubjectDTO) {
        viewModelScope.launch(dispatchers.IO) {
            markbookViewModelFlow.value = ViewModelData(uiSubjectDTO.id)
            navigationController.manage(NavigationDirections.markbookSubject.destination)
        }
    }

    fun onSubjectClick(uiSubjectDTO: UISubjectDTO) {
        viewModelScope.launch(dispatchers.IO) {
            successViewModelFlow.value = ViewModelData(uiSubjectDTO.id)
            navigationController.manage(NavigationDirections.successSubject.destination)
        }
    }

    override fun onRefresh() {
        profileInteractor.onRefresh()
    }

    fun onSettingsClick() {
        navigationController.manage(NavigationDirections.mainSettings.destination)
    }
}
