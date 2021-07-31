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

import android.util.Log
import kotlin.reflect.KClass

object Logger {
    fun d(message: String) {
        Log.d("Application", message)
    }

    fun d(clazz: KClass<*>, message: String) {
        Log.d(clazz.simpleName, message)
    }

    fun ui(message: String) {
        Log.d("UI", message)
    }
}

fun Any.d(message: String) {
    Logger.d(this::class, message)
}