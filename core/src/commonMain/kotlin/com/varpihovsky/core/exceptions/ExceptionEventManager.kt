package com.varpihovsky.core.exceptions

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

import com.varpihovsky.core.eventBus.EventBus
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull

/**
 * Manager which pushes exception into [EventBus]. It will be shown on UI lately.
 *
 * @param eventBus needed to push exception.
 *
 * @author Vladyslav Podrezenko
 */
class ExceptionEventManager(private val eventBus: EventBus) {
    /**
     * Same as [EventBus.bus] but with com.varpihovsky.ui_root.exceptions only.
     */
    val exceptions = eventBus.bus.mapNotNull { it as? Throwable }.distinctUntilChanged()

    /**
     * Pushes exception into [EventBus] to show exception on UI.
     *
     * @param exception exception to show on UI.
     */
    fun pushException(exception: Throwable) {
        eventBus.push(exception)
    }
}