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
package com.varpihovsky.ui_root.bottomBar

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.consume
import com.varpihovsky.core_lifecycle.BottomBarController
import com.varpihovsky.core_lifecycle.BottomBarEntry

class BottomBarComponent(
    componentContext: ComponentContext,
    private val onProfileNavigate: () -> Unit,
    private val onMessagesNavigate: () -> Unit
) : ComponentContext by componentContext,
    BottomBarController {
    private val _isShown = MutableValue(false)
    val isShown: Value<Boolean> = _isShown

    private val _entry = MutableValue<BottomBarEntry>(
        stateKeeper.consume<SavedState>(SAVED_STATE_KEY)?.entry ?: BottomBarEntry.Profile
    )
    val entry: Value<BottomBarEntry> = _entry

    init {
        stateKeeper.register(SAVED_STATE_KEY) { SavedState(entry.value) }
    }

    override fun select(bottomBarEntry: BottomBarEntry) {
        set(bottomBarEntry)
        when (bottomBarEntry) {
            BottomBarEntry.Profile -> onProfileNavigate()
            BottomBarEntry.Messages -> onMessagesNavigate()
        }
    }

    fun set(bottomBarEntry: BottomBarEntry) {
        _entry.value = bottomBarEntry
    }

    override fun hide() {
        _isShown.value = false
    }

    override fun show() {
        _isShown.value = true
    }

    @Parcelize
    private class SavedState(val entry: BottomBarEntry) : Parcelable

    companion object {
        private const val SAVED_STATE_KEY = "BottomBarComponentKey"
    }
}