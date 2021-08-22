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

import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_repo.repo.UserPreferencesRepo
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


internal class GeneralSettingsComponent(
    jetIQComponentContext: JetIQComponentContext
) : JetIQComponentContext by jetIQComponentContext, KoinComponent {
    // We just redirect requests and response to repo.
    // Only UI should change.
    // Suggestions should be prepared on UI directly too.
    val preferenceData: Flow<UserPreferences> by lazy { userPreferencesRepo.flow }

    private val dispatchers: CoroutineDispatchers by inject()
    private val userPreferencesRepo: UserPreferencesRepo by inject()

    private val scope = CoroutineScope(Dispatchers.Main)

    fun onPreferenceSet(userPreferences: UserPreferences) {
        scope.launch(dispatchers.IO) { userPreferencesRepo.set(userPreferences) }
    }
}