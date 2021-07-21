package com.varpihovsky.core_nav.dsl

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

import androidx.compose.runtime.Composable
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationEntry
import soup.compose.material.motion.MotionSpec

/**
 * Class that used to create [NavigationEntry].
 *
 * @see [NavigationEntry]
 *
 * @author Vladyslav Podrezenko
 */
class NavigationEntryBuilder {
    var composable: @Composable () -> Unit = {}
    var route: String = ""
    var entryType: EntryType? = null
    var inAnimation: MotionSpec? = null
    var outAnimation: MotionSpec? = null

    /**
     * Builds [NavigationEntry] from specified data.
     * Nullable fields like [entryType], [inAnimation], [outAnimation] should be specified.
     *
     * @return [NavigationEntry]
     */
    fun build(): NavigationEntry =
        NavigationEntry(
            composable,
            route,
            checkNotNull(entryType),
            checkNotNull(inAnimation),
            checkNotNull(outAnimation)
        )
}

/**
 * Class that used to create [NavigationController].
 *
 *
 * @see [NavigationController]
 *
 * @param defaultRoute route that used as start of navigation stack.
 *
 * @author Vladyslav Podrezenko
 */
class NavigationControllerBuilder(
    private val eventBus: EventBus,
    private val defaultRoute: String
) {
    private val entries = mutableListOf<NavigationEntry>()

    /**
     * Adds entry into [entries] array. Used to build [NavigationController].
     */
    fun entry(block: NavigationEntryBuilder.() -> Unit) {
        val builder = NavigationEntryBuilder().apply(block).build()
        entries.add(builder)
    }

    /**
     * Builds [NavigationController] from specified entries.
     *
     * @return [NavigationController]
     */
    fun build() = NavigationController(eventBus, entries, defaultRoute)
}