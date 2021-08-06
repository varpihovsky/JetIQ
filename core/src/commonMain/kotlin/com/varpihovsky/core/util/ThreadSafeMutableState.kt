package com.varpihovsky.core.util

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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

/**
 * Wrapper over [MutableState] class. Used to change value on every dispatcher safely.
 *
 * @param value default value.
 * @param scope scope in which value will be changed.
 *
 * @author Vladyslav Podrezenko
 */
class ThreadSafeMutableState<V>(value: V, private val scope: CoroutineScope) :
    MutableState<V> {
    private val state = mutableStateOf(value)

    override fun component1(): V {
        return state.component1()
    }

    override fun component2(): (V) -> Unit {
        return state.component2()
    }

    override var value: V
        get() = state.value
        set(value) {
            scope.launch(Dispatchers.Main) {
                state.value = value
            }
        }

    operator fun <R> setValue(
        thisRef: R,
        property: KProperty<*>,
        value: V
    ) {
        this.value = value
    }
}