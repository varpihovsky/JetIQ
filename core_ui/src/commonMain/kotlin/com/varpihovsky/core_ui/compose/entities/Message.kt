package com.varpihovsky.core_ui.compose.entities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.widgets.InfoCard
import com.varpihovsky.ui_data.dto.UIMessageDTO

@Composable
fun MessageItem(message: UIMessageDTO) {
    InfoCard(
        modifier = Modifier.padding(vertical = 5.dp),
        cardModifier = Modifier.fillMaxWidth(0.94f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 7.dp, top = 5.dp)
            ) {
                Text(text = message.sender, style = MaterialTheme.typography.h6)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp, vertical = 4.dp)
            ) {
                SelectionContainer {
                    Text(text = message.message, style = MaterialTheme.typography.body1)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = message.time, style = MaterialTheme.typography.caption)
            }
        }
    }
}