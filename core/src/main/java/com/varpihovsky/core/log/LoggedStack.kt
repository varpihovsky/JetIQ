package com.varpihovsky.core.log

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


import java.util.*

/**
 * Creates delegate which logs all changes inside Stack.
 *
 * @param policy
 */
fun <T> loggedStack(policy: LogPolicy = LogPolicy.INFO): Deque<T> =
    LoggedStack(policy, ArrayDeque())

private class LoggedStack<T>(override val policy: LogPolicy, private val deque: Deque<T>) :
    Deque<T> by deque, Logged {

    override fun addLast(e: T) {
        p(policy, "Added element to the end: $e")
        deque.addLast(e)
        printStructure()
    }

    override fun removeLast(): T = deque.removeLast().also {
        p(policy, "Removed element from the end: $it")
        printStructure()
    }

    override fun push(e: T) = addLast(e)

    override fun pop(): T = removeLast()

    override fun toString(): String = deque.toString()
}