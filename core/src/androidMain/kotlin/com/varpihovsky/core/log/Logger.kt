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

actual object Logger {
    private const val UI_TAG = "UI"

    actual fun d(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.d(clazz.simpleName, message)
    }

    actual fun ui(message: String) {
        if (BuildConfig.DEBUG)
            Log.d(UI_TAG, message)
    }

    actual fun e(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.e(clazz.simpleName, message)
    }

    actual fun w(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.w(clazz.simpleName, message)
    }

    actual fun v(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.v(clazz.simpleName, message)
    }

    actual fun i(clazz: KClass<*>, message: String) {
        if (BuildConfig.DEBUG)
            Log.i(clazz.simpleName, message)
    }
}