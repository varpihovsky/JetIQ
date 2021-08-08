package com.varpihovsky.core.util

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

/**
 * Returns same list but instead of same as "from" element, has "to" element.
 */
fun <T> List<T>.replaceAndReturn(from: T, to: T): List<T> {
    val mutable = toMutableList()
    val fromIndex = mutable.indexOf(from)
    mutable.removeAt(fromIndex)
    mutable.add(fromIndex, to)
    return mutable
}

/**
 * Returns same list but without specified element.
 */
fun <T> List<T>.remove(t: T): List<T> {
    val mutable = toMutableList()
    mutable.remove(t)
    return mutable
}

/**
 * Returns only selected and mapped to dto list.
 */
fun <T> List<Selectable<T>>.selectedOnly(): List<T> {
    return filter { it.isSelected }.map { it.dto }
}