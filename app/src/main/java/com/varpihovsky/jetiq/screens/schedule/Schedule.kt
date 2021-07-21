package com.varpihovsky.jetiq.screens.schedule

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import be.sigmadelta.calpose.model.CalposeActions
import be.sigmadelta.calpose.model.CalposeDate
import com.varpihovsky.jetiq.ui.theme.JetIQTheme
import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth

@Preview
@Composable
fun Schedule() {
    JetIQTheme(darkTheme = false) {
        val monthState = remember { mutableStateOf(YearMonth.now()) }
        val selectedDate =
            remember { mutableStateOf(CalposeDate(1, DayOfWeek.MONDAY, YearMonth.now())) }
        val forward = remember { mutableStateOf(false) }

        Calendar(
            month = monthState.value,
            actions = CalposeActions(
                onClickedNextMonth = {
                    monthState.value = monthState.value.plusMonths(1L)
                    forward.value = true
                },
                onClickedPreviousMonth = {
                    monthState.value = monthState.value.minusMonths(1L)
                    forward.value = false
                },
                onSwipedNextMonth = {
                    monthState.value = monthState.value.plusMonths(1L)
                    forward.value = true
                },
                onSwipedPreviousMonth = {
                    monthState.value = monthState.value.minusMonths(1L)
                    forward.value = false
                }
            ),
            selectedDate = selectedDate.value,
            onDayClick = { selectedDate.value = it },
            forward = forward.value
        )
    }
}

