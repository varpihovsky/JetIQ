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
    private const val DEFAULT_TAG = "Application"
    private const val UI_TAG = "UI"

    fun d(message: String) {
        Log.d(DEFAULT_TAG, message)
    }

    fun d(clazz: KClass<*>, message: String) {
        Log.d(clazz.simpleName, message)
    }

    fun ui(message: String) {
        Log.d(UI_TAG, message)
    }

    fun e(message: String) {
        Log.e(DEFAULT_TAG, message)
    }

    fun e(clazz: KClass<*>, message: String) {
        Log.e(clazz.simpleName, message)
    }

    fun w(message: String) {
        Log.w(DEFAULT_TAG, message)
    }

    fun w(clazz: KClass<*>, message: String) {
        Log.w(clazz.simpleName, message)
    }

    fun v(message: String) {
        Log.v(DEFAULT_TAG, message)
    }

    fun v(clazz: KClass<*>, message: String) {
        Log.v(clazz.simpleName, message)
    }
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