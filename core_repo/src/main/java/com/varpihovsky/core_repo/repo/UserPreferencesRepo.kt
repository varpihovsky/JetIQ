package com.varpihovsky.core_repo.repo

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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

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
    suspend fun <T> set(key: Preferences.Key<T>, value: T)

    /**
     * Clears all preferences.
     */
    suspend fun clear()

    object PreferencesKeys {
        val SHOW_NOTIFICATIONS = booleanPreferencesKey("show_notifications")
        val MARKBOOK_LIST_TYPE = stringPreferencesKey("markbook_list_type")
        val SUCCESS_LIST_TYPE = stringPreferencesKey("success_list_type")
    }

    companion object {
        operator fun invoke(dataStore: DataStore<Preferences>): UserPreferencesRepo =
            UserPreferencesRepoImpl(dataStore)
    }
}

private class UserPreferencesRepoImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepo {

    override val flow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("UserPreferencesRepo", "Failed load preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val showNotification =
                preferences[UserPreferencesRepo.PreferencesKeys.SHOW_NOTIFICATIONS] ?: true

            val markbookListType =
                SubjectListType.ofName(preferences[UserPreferencesRepo.PreferencesKeys.MARKBOOK_LIST_TYPE])

            val successListType = SubjectListType.ofName(
                preferences[UserPreferencesRepo.PreferencesKeys.SUCCESS_LIST_TYPE]
            )

            UserPreferences(showNotification, successListType, markbookListType)
        }

    override suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences -> preferences[key] = value }
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}