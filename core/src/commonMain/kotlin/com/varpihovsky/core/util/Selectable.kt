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

/**
 * Wrapper class used for selection marking.
 *
 * @author Vladyslav Podrezenko
 */
expect class Selectable<T>(dto: T, isSelected: Boolean) {
    val dto: T
    val isSelected: Boolean
}

/**
 * Returns same instance but with opposite [isSelected] field.
 *
 * @return [Selectable]
 */
fun <T> Selectable<T>.selectedToggle() = Selectable(dto, !isSelected)