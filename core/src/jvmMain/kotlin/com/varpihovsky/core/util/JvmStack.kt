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
package com.varpihovsky.core.util

actual fun <T> stack(): Stack<T> = StackImpl()

private class StackImpl<T> : Stack<T> {
    override val size: Int
        get() = deque.size

    private val deque = ArrayDeque<T>()

    override fun push(e: T) = addLast(e)

    override fun addLast(e: T) {
        deque.addLast(e)
    }

    override fun pop(): T = removeLast()

    override fun removeLast(): T = deque.removeLast()

    override fun clear() = deque.clear()

    override fun iterator(): Iterator<T> = deque.iterator()

    override fun toString(): String = deque.toString()
}