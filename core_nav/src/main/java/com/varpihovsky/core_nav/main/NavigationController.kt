package com.varpihovsky.core_nav.main

import com.varpihovsky.core.util.addAndReturn
import com.varpihovsky.core.util.removeLastAndReturn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class NavigationController(
    internal val entries: List<NavigationEntry>,
    private val defaultRoute: String
) {
    val operation by lazy { backStack.map { backStackToOperation(it) }.distinctUntilChanged() }

    private var backStackSize = 1

    private var previousEntry: NavigationEntry? = null

    private var callback: ((String) -> Unit)? = null

    private val backStack =
        MutableStateFlow(listOf(checkNotNull(entries.find { defaultRoute == it.route })))

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

    fun onBack() {
        backStack.value = backStack.value.removeLastAndReturn()

        backStack.value.lastOrNull()?.route?.let { callback?.invoke(it) }
    }

    fun manage(route: String) {
        val entry = checkNotNull(entries.find { route == it.route })

        callback?.invoke(route)

        when (entry.type) {
            EntryType.Main -> backStack.value = listOf(entry)
            EntryType.SubMenu -> backStack.value = backStack.value.addAndReturn(entry)
        }
    }

    fun setNavigationCallback(callback: (String) -> Unit) {
        this.callback = callback
    }

    private fun onFinished() {
        manage(defaultRoute)
    }
}