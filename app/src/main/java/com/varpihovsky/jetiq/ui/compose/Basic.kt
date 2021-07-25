package com.varpihovsky.jetiq.ui.compose

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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.varpihovsky.ui_data.DropDownItem

@Composable
fun CenterLayout(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        content()
    }
}

@Composable
fun CenterLayoutItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}

@Composable
fun VerticalScrollLayout(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        content()
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    cardModifier: Modifier = Modifier,
    elevation: Dp = 5.dp,
    content: @Composable () -> Unit
) {
    CenterLayoutItem(modifier = modifier) {
        Card(modifier = cardModifier, elevation = elevation) {
            content()
        }
    }
}

@Composable
fun InfoCard(
    content: @Composable () -> Unit
) {
    InfoCard(
        modifier = Modifier.padding(vertical = 20.dp),
        cardModifier = Modifier.fillMaxWidth(0.92f)
    ) {
        content()
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun InfoListPreview() {
    InfoList(title = "Успішність", info = {
        Column {
            BoxInfo(bigText = "100", smallText = "1 Семестр")
            BoxInfo(bigText = "100", smallText = "2 Семестр")
        }
    }, moreInfoTitle = "Більше...", checked = false, onToggle = {})
}

@ExperimentalAnimationApi
@Composable
fun InfoList(
    modifier: Modifier = Modifier,
    title: String,
    titlePadding: Dp = 15.dp,
    info: @Composable () -> Unit,
    moreInfoTitle: String,
    moreInfoContent: @Composable () -> Unit = {},
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        CenterLayoutItem(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(titlePadding),
                text = title,
                style = MaterialTheme.typography.h5
            )
            info()
        }
        AnimatedVisibility(visible = checked) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                moreInfoContent()
            }
        }
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
}


@Preview
@Composable
fun BoxInfoPreview() {
    BoxInfo(bigText = "ФКСА", smallText = "Факультет")
}

@Composable
fun BoxInfo(
    modifier: Modifier = Modifier,
    bigText: String,
    smallText: String
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = bigText, style = MaterialTheme.typography.subtitle1)
        }
        Row {
            Text(text = smallText, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun ErrorDialogPreview() {
    ErrorDialog(message = "Hello, World!") {}
}

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            CenterLayoutItem {
                BasicTextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = onDismiss,
                    text = "Відхилити"
                )
            }
        },
        title = {
            CenterLayoutItem {
                Text(modifier = Modifier.padding(10.dp), text = "Помилка")
            }
        },
        text = {
            CenterLayoutItem {
                Text(modifier = Modifier.padding(10.dp), text = message)
            }
        }
    )
}

@Composable
fun BasicAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = { BackIconButton(onClick = onBackClick) }
    )
}

@Composable
fun BackIconButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}

@Composable
fun BasicTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    TextButton(modifier = modifier, onClick = onClick) {
        Text(text = text)
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Composable
fun SubscribedExposedDropDownList(
    modifier: Modifier = Modifier,
    text: String,
    suggestions: List<DropDownItem>,
    selected: DropDownItem,
    onSelect: (DropDownItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SubscribedExposedDropDownList(
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
fun SubscribedExposedDropDownList(
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
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = { onSelect(label) }) {
                    Text(text = label.text)
                }
            }
        }
    }
}