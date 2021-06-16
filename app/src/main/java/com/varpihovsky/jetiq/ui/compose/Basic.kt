package com.varpihovsky.jetiq.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    content: @Composable () -> Unit
) {
    CenterLayoutItem(modifier = modifier.padding(vertical = 20.dp)) {
        Card(modifier = Modifier.fillMaxWidth(0.92f), elevation = 5.dp) {
            content()
        }
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
                    .wrapContentHeight()
            ) {
                moreInfoContent()
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = 5.dp
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
                TextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = onDismiss
                ) {
                    Text(text = "Відхилити")
                }
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