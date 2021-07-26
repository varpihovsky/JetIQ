package com.varpihovsky.jetiq.screens.settings.general

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

import androidx.lifecycle.viewModelScope
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.jetiq.screens.JetIQViewModel
import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.ui_data.DropDownItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager,
    private val userPreferencesRepo: UserPreferencesRepo
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    val showNotifications = userPreferencesRepo.flow
        .map { it.showNotifications }.distinctUntilChanged()

    val successListType = userPreferencesRepo.flow
        .map { DropDownItem.Simple(it.successListType.toString()) }
        .distinctUntilChanged()

    val markbookListType = userPreferencesRepo.flow
        .map { DropDownItem.Simple(it.markbookListType.toString()) }
        .distinctUntilChanged()

    val buttonLocation = userPreferencesRepo.flow
        .map { DropDownItem.Simple(it.profileListExpandButtonLocation.toString()) }
        .distinctUntilChanged()

    val listTypeSuggestions = listOf(SubjectListType.PARTIAL, SubjectListType.FULL)
        .map { DropDownItem.Simple(it.toString()) }

    val buttonLocationSuggestions = listOf(
        ExpandButtonLocation.LOWER, ExpandButtonLocation.UPPER
    ).map { DropDownItem.Simple(it.toString()) }

    fun onShowNotificationsSwitched(switched: Boolean) {
        viewModelScope.launch(dispatchers.IO) {
            userPreferencesRepo.set(
                UserPreferencesRepo.PreferencesKeys.SHOW_NOTIFICATIONS,
                switched
            )
        }
    }

    fun onSuccessListTypeChange(successListType: DropDownItem.Simple) {
        viewModelScope.launch {
            userPreferencesRepo.set(
                UserPreferencesRepo.PreferencesKeys.SUCCESS_LIST_TYPE,
                SubjectListType.ofString(successListType.text).name
            )
        }
    }

    fun onMarkbookListTypeChange(markbookListType: DropDownItem.Simple) {
        viewModelScope.launch {
            userPreferencesRepo.set(
                UserPreferencesRepo.PreferencesKeys.MARKBOOK_LIST_TYPE,
                SubjectListType.ofString(markbookListType.text).name
            )
        }
    }

    fun onButtonLocationChange(buttonLocation: DropDownItem.Simple) {
        viewModelScope.launch {
            userPreferencesRepo.set(
                UserPreferencesRepo.PreferencesKeys.EXPAND_BUTTON_LOCATION,
                ExpandButtonLocation.ofString(buttonLocation.text).name
            )
        }
    }
}