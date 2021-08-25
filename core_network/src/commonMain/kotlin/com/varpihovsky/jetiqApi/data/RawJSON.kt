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
package com.varpihovsky.jetiqApi.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.CharArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder

@Serializable
internal class RawJSON(val json: String) {
    @Serializer(forClass = RawJSON::class)
    companion object : KSerializer<RawJSON> {
        override fun deserialize(decoder: Decoder): RawJSON {
            val input = decoder as? JsonDecoder ?: throw RuntimeException()

            val element = input.decodeJsonElement()

            return RawJSON(input.json.encodeToString(element))
        }

        override val descriptor: SerialDescriptor
            get() = SerialDescriptor("RawJson", CharArraySerializer().descriptor)

        override fun serialize(encoder: Encoder, value: RawJSON) {
        }

    }
}