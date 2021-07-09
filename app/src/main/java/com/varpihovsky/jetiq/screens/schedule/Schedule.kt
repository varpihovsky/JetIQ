package com.varpihovsky.jetiq.screens.schedule

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

