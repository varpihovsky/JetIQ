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

import kotlinx.coroutines.*

class ReactiveTask(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val task: suspend () -> Unit
) {
    private var asyncTask: Job? = null
    private val scope = CoroutineScope(dispatcher)

    fun start() {
        if (asyncTask?.isActive == true) {
            stop()
        }

        asyncTask = scope.async {
            task()
        }
    }

    fun stop() {
        asyncTask?.cancel()
    }
}