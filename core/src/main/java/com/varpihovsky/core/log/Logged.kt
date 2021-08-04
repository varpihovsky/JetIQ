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


/**
 * Internal marker interface used to add standard methods to all logged delegates.
 */
internal interface Logged {
    val policy: LogPolicy
}

internal fun Logged.printStructure() {
    p(policy, "Structure: $this")
}

internal fun Logged.p(policy: LogPolicy, message: String) {
    when (policy) {
        LogPolicy.DEBUG -> d(message)
        LogPolicy.VERBOSE -> v(message)
        LogPolicy.INFO -> i(message)
        LogPolicy.WARN -> w(message)
        LogPolicy.ERROR -> e(message)
    }
}
