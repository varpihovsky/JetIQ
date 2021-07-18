package com.varpihovsky.core_nav.dsl

import androidx.compose.runtime.Composable
import com.varpihovsky.core_nav.main.EntryType
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationEntry
import soup.compose.material.motion.MotionSpec

class NavigationEntryBuilder {
    var composable: @Composable () -> Unit = {}
    var route: String = ""
    var entryType: EntryType? = null
    var inAnimation: MotionSpec? = null
    var outAnimation: MotionSpec? = null

    fun build(): NavigationEntry =
        NavigationEntry(
            composable,
            route,
            checkNotNull(entryType),
            checkNotNull(inAnimation),
            checkNotNull(outAnimation)
        )
}

class NavigationControllerBuilder(private val defaultRoute: String) {
    private val entries = mutableListOf<NavigationEntry>()

    fun entry(block: NavigationEntryBuilder.() -> Unit) {
        val builder = NavigationEntryBuilder().apply(block).build()
        entries.add(builder)
    }

    fun build() = NavigationController(entries, defaultRoute)
}