package com.varpihovsky.core_nav.dsl

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