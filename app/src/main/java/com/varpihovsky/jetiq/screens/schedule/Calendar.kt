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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.sigmadelta.calpose.CalposeStatic
import be.sigmadelta.calpose.WEIGHT_7DAY_WEEK
import be.sigmadelta.calpose.model.CalposeActions
import be.sigmadelta.calpose.model.CalposeDate
import be.sigmadelta.calpose.model.CalposeProperties
import be.sigmadelta.calpose.model.CalposeWidgets
import be.sigmadelta.calpose.widgets.DefaultDay
import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import soup.compose.material.motion.*
import java.util.*

@ExperimentalAnimationApi
@Composable
fun Calendar(
    month: YearMonth,
    actions: CalposeActions,
    selectedDate: CalposeDate,
    onDayClick: (CalposeDate) -> Unit,
    forward: Boolean
) {
    CalposeStatic(
        month = month,
        actions = actions,
        widgets = CalposeWidgets(
            header = { current, todayMonth, calposeActions ->
                MaterialHeader(
                    month = current,
                    todayMonth = todayMonth,
                    actions = calposeActions,
                    backgroundColor = MaterialTheme.colors.primary,
                    titleContainer = { title ->
                        MaterialSharedAxisX(targetState = month, forward = forward) {
                            title()
                        }
                    }
                )
            },
            headerDayRow = { headerDayList -> CalendarHeaderDayRow(headerDayList = headerDayList) },
            day = { dayDate, todayDate ->
                CalendarDay(
                    dayDate = dayDate,
                    todayDate = todayDate,
                    selectedDate = selectedDate,
                    onDayClick = onDayClick
                )
            },
            priorMonthDay = { dayDate ->
                DefaultDay(
                    text = dayDate.day.toString(),
                    style = TextStyle(color = Color.LightGray),
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .weight(WEIGHT_7DAY_WEEK)
                )
            },
            monthContainer = {
                MaterialMotion(
                    targetState = month,
                    motionSpec = when (Axis.X) {
                        Axis.X -> materialSharedAxisX(
                            forward = forward,
                            slideDistance = rememberSlideDistance()
                        )
                        Axis.Y -> materialSharedAxisY(
                            forward = forward,
                            slideDistance = rememberSlideDistance()
                        )
                        Axis.Z -> materialSharedAxisZ(forward = forward)
                    },
                    modifier = Modifier,
                    content = {
                        Column {
                            it()
                        }
                    }
                )
            }
        ),
        properties = CalposeProperties(
            changeMonthAnimation = tween(1000),
            changeMonthSwipeTriggerVelocity = 200
        )
    )
}

@Composable
fun MaterialHeader(
    month: YearMonth,
    todayMonth: YearMonth,
    actions: CalposeActions,
    backgroundColor: Color,
    titleContainer: @Composable (@Composable () -> Unit) -> Unit = { it() }
) {
    val isCurrentMonth = todayMonth == month
    Row(
        modifier = Modifier.background(color = backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { actions.onClickedPreviousMonth() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Left"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        titleContainer {
            DefaultMonthTitle(
                month = month,
                isCurrentMonth = isCurrentMonth,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { actions.onClickedNextMonth() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Right"
            )
        }
    }
}

@Composable
internal fun DefaultMonthTitle(
    month: YearMonth,
    isCurrentMonth: Boolean = false,
    textStyle: TextStyle = TextStyle()
) {
    val title = remember(month) {
        val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
        month.format(formatter)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    Text(
        text = title,
        modifier = Modifier.padding(vertical = 8.dp),
        style = TextStyle(
            fontWeight = if (isCurrentMonth) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 22.sp,
        ).merge(textStyle),
        color = MaterialTheme.colors.onPrimary
    )
}


@Composable
fun CalendarHeaderDayRow(
    headerDayList: Set<DayOfWeek>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 16.dp),
    ) {
        headerDayList.forEach {
            DefaultDay(
                text = getDayHeaderText(it),
                modifier = Modifier.weight(WEIGHT_7DAY_WEEK),
                style = TextStyle(color = Color.Gray)
            )
        }
    }

}

private fun getDayHeaderText(dayOfWeek: DayOfWeek) =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Пн"
        DayOfWeek.TUESDAY -> "Вт"
        DayOfWeek.WEDNESDAY -> "Ср"
        DayOfWeek.THURSDAY -> "Чт"
        DayOfWeek.FRIDAY -> "Пт"
        DayOfWeek.SATURDAY -> "Сб"
        DayOfWeek.SUNDAY -> "Нд"
    }

@Composable
fun RowScope.CalendarDay(
    dayDate: CalposeDate,
    todayDate: CalposeDate,
    selectedDate: CalposeDate,
    onDayClick: (CalposeDate) -> Unit
) {
    val isToday = dayDate == todayDate
    val isSelected = dayDate == selectedDate
    val dayHasPassed = dayDate.day < todayDate.day
    val isCurrentMonth = dayDate.month == todayDate.month

    val widget: @Composable () -> Unit = {
        val weight = if (isToday) 1f else WEIGHT_7DAY_WEEK
        CalendarDay(
            modifier = Modifier
                .weight(weight, true)
                .clickable(onClick = { onDayClick(dayDate) }),
            text = dayDate.day.toString(),
            color = when {
                isToday || isSelected -> MaterialTheme.colors.onPrimary
                isCurrentMonth && dayHasPassed -> MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                else -> MaterialTheme.colors.onBackground
            },
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
        )
    }

    when {
        isSelected -> {
            MarkedDay(
                modifier = Modifier.weight(WEIGHT_7DAY_WEEK),
                color = MaterialTheme.colors.primary,
                content = widget
            )
        }
        isToday -> {
            MarkedDay(
                modifier = Modifier.weight(WEIGHT_7DAY_WEEK),
                color = MaterialTheme.colors.secondary,
                content = widget
            )
        }
        else -> widget()
    }
}

@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    fontWeight: FontWeight
) {
    DefaultDay(
        text = text,
        modifier = modifier.padding(4.dp),
        style = TextStyle(
            color = color,
            fontWeight = fontWeight
        )
    )
}

@Composable
fun MarkedDay(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}