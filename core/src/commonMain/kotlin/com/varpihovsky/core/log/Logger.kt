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

import kotlin.reflect.KClass

/**
 * Wrapper class for [Log] which automatically removes all logs when it is not debug build.
 */
expect object Logger {
    fun d(clazz: KClass<*>, message: String)

    fun ui(message: String)

    fun e(clazz: KClass<*>, message: String)

    fun w(clazz: KClass<*>, message: String)

    fun v(clazz: KClass<*>, message: String)

    fun i(clazz: KClass<*>, message: String)
}

fun Any.d(message: String) {
    Logger.d(this::class, message)
}

fun Any.e(message: String) {
    Logger.e(this::class, message)
}

fun Any.w(message: String) {
    Logger.w(this::class, message)
}

fun Any.v(message: String) {
    Logger.v(this::class, message)
}

fun Any.i(message: String) {
    Logger.i(this::class, message)
}