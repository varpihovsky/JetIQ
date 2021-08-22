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

import com.varpihovsky.core_db.dao.PreferencesDAO
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

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
    suspend fun set(preferences: UserPreferences)

    /**
     * Clears all preferences.
     */
    suspend fun clear()

    companion object {
        operator fun invoke(preferencesDAO: PreferencesDAO): UserPreferencesRepo =
            UserPreferencesRepoImpl(preferencesDAO)
    }
}

private class UserPreferencesRepoImpl(private val preferencesDAO: PreferencesDAO) : UserPreferencesRepo {
    override val flow: Flow<UserPreferences>
        get() = preferencesDAO.get().filterNotNull()

    override suspend fun set(preferences: UserPreferences) {
        preferencesDAO.set(preferences)
    }

    override suspend fun clear() {
        preferencesDAO.delete()
    }
}