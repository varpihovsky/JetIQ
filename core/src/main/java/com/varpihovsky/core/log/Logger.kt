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

import android.util.Log
import com.varpihovsky.core.BuildConfig
import kotlin.reflect.KClass

/**
 * Wrapper class for [Log] which automatically removes all logs when it is not debug build.
 */
object Logger {
    private const val DEFAULT_TAG = "Application"
    private const val UI_TAG = "UI"

    fun d(message: String) {
        if (BuildConfig.DEBUG)
            Log.d(DEFAULT_TAG, message)
    }

    fun d(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.d(clazz.simpleName, message)
    }

    fun ui(message: String) {
        if (BuildConfig.DEBUG)
            Log.d(UI_TAG, message)
    }

    fun e(message: String) {
        if (BuildConfig.DEBUG)
            Log.e(DEFAULT_TAG, message)
    }

    fun e(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.e(clazz.simpleName, message)
    }

    fun w(message: String) {
        if (BuildConfig.DEBUG)
            Log.w(DEFAULT_TAG, message)
    }

    fun w(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.w(clazz.simpleName, message)
    }

    fun v(message: String) {
        if (BuildConfig.DEBUG)
            Log.v(DEFAULT_TAG, message)
    }

    fun v(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.v(clazz.simpleName, message)
    }

    fun i(message: String) {
        if (BuildConfig.DEBUG)
            Log.i(DEFAULT_TAG, message)
    }

    fun i(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.i(clazz.simpleName, message)
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

fun Any.i(message: String) {
    Logger.i(this::class, message)
}