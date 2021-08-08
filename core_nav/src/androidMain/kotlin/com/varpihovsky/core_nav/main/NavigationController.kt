package com.varpihovsky.core_nav.main

import android.os.Bundle
import androidx.core.os.bundleOf
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core.log.d

class AndroidNavigationController(
    eventBus: EventBus,
    entries: List<NavigationEntry>,
    defaultRoute: String
) : NavigationController(eventBus, entries, defaultRoute) {
    internal fun saveState(): Bundle {
        return bundleOf(
            DEFAULT_ROUTE_KEY to defaultRoute,
            BACKSTACK_KEY to backStack.map { it.route }.toTypedArray()
        ).also {
            d("Saving state...\nSaved: $it")
        }
    }

    internal fun restoreState(bundle: Bundle?) {
        bundle?.apply {
            d("Restoring state...\nRestored: $this")
            getString(DEFAULT_ROUTE_KEY)?.let {
                defaultRoute = it
            }
            getStringArray(BACKSTACK_KEY)?.let { saved ->
                mapRoutesToEntries(saved as Array<String>).forEach { backStack.push(it) }
                previousEntry =
                    if (backStack.size >= 2) backStack.elementAt(backStack.size - 2) else null
                backStackSize = backStack.size
                eventBus.push(backStack.toList())
            }
        }
    }

    private fun mapRoutesToEntries(savedStack: Array<String>): List<NavigationEntry> {
        return savedStack
            .mapNotNull { route -> entries.find { it.route == route } }
            .toList().also {
                d("Mapped routes: $savedStack to $it")
            }
    }
}