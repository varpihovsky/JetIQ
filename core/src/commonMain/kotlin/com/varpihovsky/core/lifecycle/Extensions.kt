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
package com.varpihovsky.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varpihovsky.core.di.get

/**
 * Creates new state in di container. Use it when rememberSavable doesn't work.
 *
 * @see SimpleState
 */
@Composable
fun <T> createSimpleState(key: String, state: T): SimpleState<T> {
    val stateHolder = get<SimpleStateHolder>()
    remember(key, state) {
        stateHolder.save(key, SimpleState(state))
    }
    return remember(key) { stateHolder.restore(key) }
}