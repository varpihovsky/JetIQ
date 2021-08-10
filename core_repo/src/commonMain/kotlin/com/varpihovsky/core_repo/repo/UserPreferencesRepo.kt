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
package com.varpihovsky.core_repo.repo

import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface used for providing preferences data of current user.
 *
 * @author Vladyslav Podrezenko
 */
interface UserPreferencesRepo {
    /**
     * Value that provides actual data about current user preferences.
     */
    val flow: Flow<UserPreferences>

    /**
     * Method used to set one key of preference. Later changes will be provided in [flow].
     */
    suspend fun <T> set(key: PreferencesKeys, value: T)

    /**
     * Clears all preferences.
     */
    suspend fun clear()
}

// We have empty class because there can be any preferences on any platform
expect sealed class PreferencesKeys

