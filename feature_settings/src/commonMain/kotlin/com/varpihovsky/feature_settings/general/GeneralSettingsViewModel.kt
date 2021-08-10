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
package com.varpihovsky.feature_settings.general

import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core.lifecycle.viewModelScope
import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.JetIQViewModel
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_repo.repo.PreferencesKeys
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class GeneralSettingsViewModel(
    private val dispatchers: CoroutineDispatchers,
    appbarManager: AppbarManager,
    navigationController: NavigationController,
    exceptionEventManager: ExceptionEventManager,
    private val userPreferencesRepo: UserPreferencesRepo
) : JetIQViewModel(appbarManager, navigationController, exceptionEventManager) {
    // We just redirect requests and response to repo.
    // Only UI should change.
    // Suggestions should be prepared on UI directly too.
    val preferenceData: Flow<UserPreferences> = userPreferencesRepo.flow

    fun <T> onPreferenceSet(preferenceKey: PreferencesKeys, value: T) {
        viewModelScope.launch(dispatchers.IO) { userPreferencesRepo.set(preferenceKey, value) }
    }
}