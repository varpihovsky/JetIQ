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
    val markbookListType: SubjectListType = SubjectListType.PARTIAL,
    val profileListExpandButtonLocation: ExpandButtonLocation = ExpandButtonLocation.LOWER
)

// These classes could be multiplatform but Exceptions can't be shared

expect enum class SubjectListType {
    FULL,
    PARTIAL;

    abstract override fun toString(): String

    companion object {
        fun ofName(name: String?): SubjectListType

        fun ofString(string: String): SubjectListType
    }
}

expect enum class ExpandButtonLocation {
    LOWER,
    UPPER;

    abstract override fun toString(): String

    companion object {
        fun ofName(name: String?): ExpandButtonLocation

        fun ofString(string: String): ExpandButtonLocation
    }
}