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

import com.varpihovsky.core.exceptions.WrongDataException
import com.varpihovsky.core_repo.repo.PreferencesKeys
import com.varpihovsky.repo_data.ExpandButtonLocation
import com.varpihovsky.repo_data.SubjectListType

internal actual object PreferenceValueMapper {
    actual inline fun <reified T> map(key: PreferencesKeys, initial: T): Any {
        if (!isValueRight(key, initial)) {
            throw WrongDataException("Received wrong initial value.")
        }

        return when (key) {
            PreferencesKeys.EXPAND_BUTTON_LOCATION -> ExpandButtonLocation.ofString(initial as String)
            PreferencesKeys.MARKBOOK_LIST_TYPE -> SubjectListType.ofString(initial as String)
            PreferencesKeys.SHOW_NOTIFICATION -> initial as Boolean
            PreferencesKeys.SUCCESS_LIST_TYPE -> SubjectListType.ofString(initial as String)
        }
    }

    private inline fun <reified T> isValueRight(key: PreferencesKeys, value: T): Boolean =
        when (key) {
            PreferencesKeys.EXPAND_BUTTON_LOCATION -> value is String && (
                    value == ExpandButtonLocation.LOWER.toString() ||
                            value == ExpandButtonLocation.UPPER.toString())
            PreferencesKeys.MARKBOOK_LIST_TYPE -> value is String && isListTypeValueRight(value)
            PreferencesKeys.SHOW_NOTIFICATION -> value is Boolean
            PreferencesKeys.SUCCESS_LIST_TYPE -> value is String && isListTypeValueRight(value)
        }

    private fun isListTypeValueRight(value: String): Boolean =
        value == SubjectListType.FULL.toString() || value == SubjectListType.PARTIAL.toString()


}