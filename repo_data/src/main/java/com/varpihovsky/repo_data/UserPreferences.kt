package com.varpihovsky.repo_data

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

data class UserPreferences(
    val showNotifications: Boolean = true,
    val successListType: SubjectListType = SubjectListType.PARTIAL,
    val markbookListType: SubjectListType = SubjectListType.PARTIAL
)

enum class SubjectListType {
    FULL {
        override fun toString(): String = FULL_STRING

    },
    PARTIAL {
        override fun toString(): String = PARTIAL_STRING
    };

    abstract override fun toString(): String

    companion object {
        private const val FULL_STRING = "Повний"
        private const val PARTIAL_STRING = "По семестру"

        fun ofName(name: String?) = valueOf(name ?: PARTIAL.name)

        fun ofString(string: String) = when (string) {
            FULL_STRING -> FULL
            PARTIAL_STRING -> PARTIAL
            else -> throw IllegalStateException("Wrong string!")
        }
    }
}
