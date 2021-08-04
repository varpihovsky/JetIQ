package com.varpihovsky.core.appbar

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Manager used to push appbar changes into [EventBus] to show on UI.
 *
 * @author Vladyslav Podrezenko
 */
class AppbarManager @Inject constructor(private val eventBus: EventBus) {

    /**
     * Same as [EventBus.bus] but mapped to [AppbarCommand].
     */
    val commands: StateFlow<AppbarCommand> = eventBus.bus
        .mapNotNull { it as? AppbarCommand }
        .distinctUntilChanged()
        .stateIn(
            scope = CoroutineScope(Dispatchers.Unconfined),
            started = SharingStarted.Eagerly,
            initialValue = AppbarCommand.Empty
        )

    /**
     * Pushes [command][AppbarCommand] into [EventBus].
     *
     * @param command command to push into [EventBus]
     */
    fun manage(command: AppbarCommand) {
        eventBus.push(command)
    }
}