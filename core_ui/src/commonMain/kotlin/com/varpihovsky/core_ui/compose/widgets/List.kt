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
package com.varpihovsky.core_ui.compose.widgets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.foundation.AnimatedVisibilityComposable
import com.varpihovsky.core_ui.compose.foundation.CenterLayoutItem
import com.varpihovsky.ui_data.dto.DropDownItem

@ExperimentalAnimationApi
@Composable
fun ExpandableList(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    val checked = remember { mutableStateOf(false) }

    ExpandableList(
        modifier = modifier,
        title = title,
        checked = checked.value,
        onCheckedChange = { checked.value = it }) {
        content()
    }
}

@ExperimentalAnimationApi
@Composable
fun ExpandableList(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val iconRotateAnimation = animateFloatAsState(targetValue = if (checked) 180f else 0f)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
            )
            IconToggleButton(checked = checked, onCheckedChange = onCheckedChange) {
                Icon(
                    modifier = Modifier.rotate(iconRotateAnimation.value),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
        AnimatedVisibilityComposable(visible = checked) {
            content()
        }
    }
}

@Composable
fun ColumnScope.ListExpandButton(
    checked: Boolean,
    moreInfoTitle: String,
    onToggle: (Boolean) -> Unit,
    titlePadding: Dp
) {
    CenterLayoutItem(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(4f)
                .padding(titlePadding),
            text = moreInfoTitle,
            style = MaterialTheme.typography.h6,
        )
        IconToggleButton(
            modifier = Modifier.weight(1f),
            checked = checked,
            onCheckedChange = onToggle
        ) {
            val animatedRotation =
                animateFloatAsState(targetValue = if (checked) 180f else 0f)
            Icon(
                modifier = Modifier.rotate(animatedRotation.value),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ExposedDropDownList(
    modifier: Modifier = Modifier,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropDownList(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        suggestions = suggestions,
        selected = selected,
        onSelect = {
            expanded = false
            onSelect(it)
        }
    )
}

@Composable
fun ExposedDropDownList(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    val iconRotateAnimation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    val icon = Icons.Filled.ArrowDropDown

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selected.text,
            enabled = false,
            maxLines = 3,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .focusable(false)
                .clickable { onExpandedChange(!expanded) },
            trailingIcon = {
                IconToggleButton(
                    checked = expanded,
                    onCheckedChange = onExpandedChange
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.rotate(iconRotateAnimation)
                    )
                }
            }
        )
        DropDownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            Column(modifier = Modifier.heightIn(max = 150.dp).verticalScroll(rememberScrollState())) {
                suggestions.forEach { label ->
                    DropDownMenuItem(onClick = { onSelect(label) }) {
                        Text(text = label.text)
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalSubscribedExposedDropDownList(
    modifier: Modifier = Modifier,
    text: String,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = text,
            style = MaterialTheme.typography.h6
        )
        ExposedDropDownList(
            suggestions = suggestions,
            selected = selected,
            onSelect = onSelect
        )
    }
}

@Composable
fun HorizontalSubscribedExposedDropDownList(
    modifier: Modifier = Modifier,
    text: String,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    HorizontalSubscribedExposedDropDownList(
        modifier = modifier,
        text = text,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        suggestions = suggestions,
        selected = selected,
        onSelect = {
            expanded = false
            onSelect(it)
        }
    )
}

@Composable
fun HorizontalSubscribedExposedDropDownList(
    modifier: Modifier = Modifier,
    text: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(modifier = Modifier.weight(1.5f), text = text)
        ExposedDropDownList(
            modifier = Modifier.weight(3f),
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            suggestions = suggestions,
            selected = selected,
            onSelect = onSelect
        )
    }
}