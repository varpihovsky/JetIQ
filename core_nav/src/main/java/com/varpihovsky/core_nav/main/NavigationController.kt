package com.varpihovsky.core_nav.main

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.varpihovsky.core.eventBus.EventBus
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.*

/**
 * Class which is used to control navigation stack. Designed to work only with composable functions.
 *
 * @author Vladyslav Podrezenko
 */
class NavigationController(
    private val eventBus: EventBus,
    internal var entries: List<NavigationEntry>,
    private var defaultRoute: String
) {
    internal val operation = eventBus.bus
        .mapNotNull { it as? List<NavigationEntry> }
        .map { backStackToOperation(it) }

    private val backStack: Stack<NavigationEntry> = Stack()

    private var backStackSize = 1

    private var previousEntry: NavigationEntry? = null

    private var callback: ((String) -> Unit)? = null

    init {
        val default = getDefault()

        if (default != null) {
            resetBackStack(default)
        } else {
            Log.d("NavigationController", "Haven't found default route. Probably restoring state.")
        }
    }

    private fun backStackToOperation(stack: List<NavigationEntry>): NavigationOperation {
        if (stack.isEmpty()) {
            return NavigationOperation.Finish(this::onFinished)
        }

        val entry = stack.last()
        val motionSpec = getMotionSpecByEntry(entry, stack)

        previousEntry = entry
        backStackSize = stack.size

        return NavigationOperation.Navigate(entry.route, entry.composable, motionSpec)
    }

    private fun getMotionSpecByEntry(entry: NavigationEntry, stack: List<NavigationEntry>) = when {
        isSubEntryIn(entry, stack) -> entry.inAnimation
        isSubEntryOut(entry, stack) -> entry.outAnimation
        isMainEntryRightOne(entry) -> {
            if (previousEntry?.type == EntryType.SubMenu) {
                previousEntry!!.outAnimation
            } else {
                entry.inAnimation
            }
        }
        isMainEntryLeftOne(entry) -> entry.outAnimation
        else -> entry.outAnimation
    }

    private fun isMainEntryRightOne(entry: NavigationEntry) =
        entry.type == EntryType.Main && entries.indexOf(entry) > entries.indexOf(previousEntry)

    private fun isMainEntryLeftOne(entry: NavigationEntry) =
        entry.type == EntryType.Main && entries.indexOf(entry) < entries.indexOf(previousEntry)

    private fun isSubEntryIn(entry: NavigationEntry, stack: List<NavigationEntry>) =
        entry.type == EntryType.SubMenu && stack.size > backStackSize

    private fun isSubEntryOut(entry: NavigationEntry, stack: List<NavigationEntry>) =
        entry.type == EntryType.SubMenu && stack.size < backStackSize

    /**
     * Removes last element from backstack and than invokes callback. If last entry was removed
     * application is finishing.
     */
    fun onBack() {
        backStack.pop()
        eventBus.push(backStack.toList())

        backStack.lastOrNull()?.route?.let { callback?.invoke(it) }
    }

    /**
     * Finds route in the entries and than adds it to stack. Behaviour depends on EntryType.
     * Invokes callback before actually navigating. But invokes after navigating back in [onBack] method.
     *
     * @see [EntryType]
     */
    fun manage(route: String) {
        val entry = checkNotNull(entries.find { route == it.route })

        callback?.invoke(route)

        when (entry.type) {
            EntryType.Main -> resetBackStack(entry)
            EntryType.SubMenu -> eventBus.push(backStack.apply { push(entry) }.toList())
        }
    }

    /**
     * Sets callback which will be invoked before every navigation.
     */
    fun setNavigationCallback(callback: (String) -> Unit) {
        this.callback = callback
    }

    /**
     * Saves state and puts it into bundle. Used only in
     * [Navigation Saver][com.varpihovsky.core_nav.dsl.navigationControllerSaver].
     */
    fun saveState(): Bundle {
        return bundleOf(
            ENTRIES_KEY to entries.toTypedArray(),
            DEFAULT_ROUTE_KEY to defaultRoute,
            BACKSTACK_KEY to backStack.toTypedArray()
        )
    }

    /**
     * Restores state from gained bundle. Used only in
     * [Navigation Saver][com.varpihovsky.core_nav.dsl.navigationControllerSaver].
     */
    fun restoreState(bundle: Bundle?) {
        bundle?.apply {
            getParcelableArray(ENTRIES_KEY)?.toList()?.let {
                entries = it as List<NavigationEntry>
            }
            getString(DEFAULT_ROUTE_KEY)?.let {
                defaultRoute = it
            }
            getParcelableArray(BACKSTACK_KEY)?.let { saved ->
                saved.forEach { backStack.push(it as NavigationEntry) }
                previousEntry =
                    if (backStack.size >= 2) backStack.elementAt(backStack.size - 2) else null
                backStackSize = backStack.size
                eventBus.push(backStack.toList())
            }
        }
    }

    private fun getDefault() = entries.find { it.route == defaultRoute }

    private fun resetBackStack(entry: NavigationEntry) {
        backStack.clear()
        backStack.push(entry)
        eventBus.push(listOf(entry))
    }

    private fun onFinished() {
        manage(defaultRoute)
    }

    companion object {
        private const val ENTRIES_KEY = "entries"
        private const val DEFAULT_ROUTE_KEY = "route"
        private const val BACKSTACK_KEY = "backstack"
    }
}