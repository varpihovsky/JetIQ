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
package com.varpihovsky.repo_data

import com.varpihovsky.repo_data.lists.ContactList
import kotlinx.serialization.Serializable

@Serializable
class ContactDTO(
    override val id: Int,
    val text: String,
    val type: String
) : Listable<ContactList> {
    override fun with(id: Int): Listable<ContactList> = ContactDTO(id, text, type)

    companion object {
        const val TYPE_STUDENT = "student"
        const val TYPE_TEACHER = "teacher"
    }
}