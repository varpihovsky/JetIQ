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
package com.varpihovsky.ui_data.mappers

import com.varpihovsky.jetiqApi.data.Message
import com.varpihovsky.ui_data.dto.UIMessageDTO
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

actual fun Message.toUIDTO(): UIMessageDTO {
    val split = body!!.split("<b>", "</b>:<br>", "</b>")
    return UIMessageDTO(
        id.toInt(),
        split[1],
        split[2],
        Instant.ofEpochSecond(time.toLong()).let {
            DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")
                .format(LocalDateTime.ofInstant(it, ZoneId.systemDefault()))
        }
    )
}