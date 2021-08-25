package com.varpihovsky.core.eventBus

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

import com.varpihovsky.core.log.v
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * Class used to have centralized flow for events. Subscribers will be invoked when [bus] value is changed
 * but doesn't remembers any history of changes.
 *
 * @author Vladyslav Podrezenko
 */
class EventBus {
    val bus: Flow<Any>
        get() = _bus.filterNotNull()

    private val _bus = MutableStateFlow<Any?>(null)

    /**
     * Pushes event into [bus].
     *
     * @param event event to push.
     */
    fun push(event: Any) {
        v("Received event: $event")

        _bus.value = event
    }
}