package com.varpihovsky.core_network.multiplatform

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

internal fun provideHeaders(okhttpHeaders: okhttp3.Headers): Headers = HeadersAdapter(okhttpHeaders)

private class HeadersAdapter(private val okhttpHeaders: okhttp3.Headers) : Headers {
    override fun get(name: String): String? = okhttpHeaders[name]

    override val size: Int
        get() = okhttpHeaders.size

    override fun name(index: Int): String = okhttpHeaders.name(index)

    override fun value(index: Int): String = okhttpHeaders.value(index)

    override fun names(): Set<String> = okhttpHeaders.names()

    override fun values(name: String): List<String> = okhttpHeaders.values(name)

    override fun byteCount(): Long = okhttpHeaders.byteCount()

    override fun toMultimap(): Map<String, List<String>> = okhttpHeaders.toMultimap()
}