package com.varpihovsky.ui_data.dto

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

expect class UIReceiverDTO {
    val id: Int
    val text: String
    val type: ReceiverType
}

fun UIReceiverDTO.getPhotoURL() = type.getPhotoMethodURL() + id

enum class ReceiverType {
    STUDENT {
        override fun getPhotoMethodURL(): String =
            "https://iq.vntu.edu.ua/b06175/getfile.php?stud_id="

        override fun toInt() = 0
    },
    TEACHER {
        override fun getPhotoMethodURL(): String =
            "https://iq.vntu.edu.ua/method/get_photo.php?id="

        override fun toInt(): Int = 1
    };


    abstract fun getPhotoMethodURL(): String

    abstract fun toInt(): Int
}
