package com.varpihovsky.jetiq.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CenterLayout(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

@Composable
fun CenterLayoutItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        content()
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