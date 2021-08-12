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

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType
import com.varpihovsky.repo_data.UserPreferences
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

internal fun providePreferences(
    dataStore: DataStore<Preferences>,
    exceptionEventManager: ExceptionEventManager
): UserPreferencesRepo = UserPreferencesRepoImpl(dataStore, exceptionEventManager)

private class UserPreferencesRepoImpl(
    private val dataStore: DataStore<Preferences>,
    private val exceptionEventManager: ExceptionEventManager
) : UserPreferencesRepo {

    override val flow = dataStore.data.catch { manageException(it) }.map(this::mapPreferences)

    private suspend fun FlowCollector<Preferences>.manageException(exception: Throwable) {
        if (exception is IOException) {
            Log.e("UserPreferencesRepo", "Failed load preferences.", exception)
            emit(emptyPreferences())
        } else {
            exceptionEventManager.pushException(exception)
        }
    }

    private fun mapPreferences(preferences: Preferences): UserPreferences {
        val showNotification = preferences[Keys.show_notification] ?: true

        val markbookListType = SubjectListType.ofName(preferences[Keys.markbook_list_type])

        val successListType = SubjectListType.ofName(preferences[Keys.success_list_type])

        val expandButtonLocation =
            ExpandButtonLocation.ofName(preferences[Keys.expand_button_location])

        return UserPreferences(
            showNotification,
            successListType,
            markbookListType,
            expandButtonLocation
        )
    }

    override suspend fun <T> set(key: PreferencesKeys, value: T) {
        dataStore.edit { preferences ->
            mapKey<T>(key)?.let { preferences[it] = value }
        }
    }

    private fun <T> mapKey(key: PreferencesKeys): Preferences.Key<T>? =
        when (key) {
            PreferencesKeys.SHOW_NOTIFICATION -> Keys.show_notification as Preferences.Key<T>
            PreferencesKeys.MARKBOOK_LIST_TYPE -> Keys.markbook_list_type as Preferences.Key<T>
            PreferencesKeys.SUCCESS_LIST_TYPE -> Keys.success_list_type as Preferences.Key<T>
            PreferencesKeys.EXPAND_BUTTON_LOCATION -> Keys.expand_button_location as Preferences.Key<T>
            else -> null
        }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}

private object Keys {
    val show_notification = booleanPreferencesKey("show_notifications")
    val markbook_list_type = stringPreferencesKey("markbook_list_type")
    val success_list_type = stringPreferencesKey("success_list_type")
    val expand_button_location = stringPreferencesKey("expand_button_location")
}

actual sealed class PreferencesKeys {
    object SHOW_NOTIFICATION : PreferencesKeys()
    object MARKBOOK_LIST_TYPE : PreferencesKeys()
    object SUCCESS_LIST_TYPE : PreferencesKeys()
    object EXPAND_BUTTON_LOCATION : PreferencesKeys()
}