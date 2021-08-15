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

import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.KClass

actual object Logger {
    private val logger = Logger.getGlobal()
    private const val UI_TAG = "UI"

    actual fun d(clazz: KClass<*>, message: String) {
        logger.log(Level.FINE, formMessage(clazz, message))
    }

    actual fun ui(message: String) {
        logger.log(Level.FINE, "UI: $message")
    }

    actual fun e(clazz: KClass<*>, message: String) {
        logger.log(Level.SEVERE, formMessage(clazz, message))
    }

    actual fun w(clazz: KClass<*>, message: String) {
        logger.log(Level.WARNING, formMessage(clazz, message))
    }

    actual fun v(clazz: KClass<*>, message: String) {
        logger.log(Level.FINEST, formMessage(clazz, message))
    }

    actual fun i(clazz: KClass<*>, message: String) {
        logger.log(Level.INFO, formMessage(clazz, message))
    }

    private fun formMessage(clazz: KClass<*>, message: String) = "${clazz.simpleName}: $message"
}