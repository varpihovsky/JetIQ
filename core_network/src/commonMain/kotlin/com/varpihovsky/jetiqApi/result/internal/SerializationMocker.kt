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
package com.varpihovsky.jetiqApi.result.internal

import com.varpihovsky.jetiqApi.data.RawJSON
import io.ktor.client.statement.*

internal class SerializationMocker {
    fun mock(payload: HttpResponseContainer): HttpResponseContainer {
        val type = payload.expectedType
        return payload.copy(
            expectedType = type.copy(
                // Due to Result class haven't own serializer, KotlinxSerializer takes
                // serializer from this type
                kotlinType = type.kotlinType?.arguments?.first()?.type,
                // If requested type is RawJSON, we should mock serializer with it own
                type = if (type.reifiedType.toString()
                        .contains("RawJSON")
                ) RawJSON::class else type.type
            ),
        )
    }
}