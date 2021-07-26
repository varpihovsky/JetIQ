package com.varpihovsky.jetiq.screens.profile

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

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.State
import com.varpihovsky.core.Refreshable
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.navigation.NavigationDirections
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val navigationController: NavigationController,
    appbarManager: AppbarManager,
    exceptionEventManager: ExceptionEventManager,
    private val userPreferencesRepo: UserPreferencesRepo
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager), Refreshable {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)
    override val isLoading: State<Boolean>
        get() = profileInteractor.isLoading

    val profile = profileInteractor.profileData

    val successMarksInfo = profileInteractor.successData.map { it.first }
    val successSubjects = profileInteractor.successData.map { it.second }

    val markbookMarksInfo = profileInteractor.markbookData.map { it.first }
    val markbookSubjects = profileInteractor.markbookData.map { it.second }

    private val successChecked = mutableStateOf(false)
    private val markbookChecked = mutableStateOf(false)

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
        Log.d(null, "Created")
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

    override fun onRefresh() {
        profileInteractor.onRefresh()
    }

    fun onSettingsClick() {
        navigationController.manage(NavigationDirections.mainSettings.destination)
    }
}
