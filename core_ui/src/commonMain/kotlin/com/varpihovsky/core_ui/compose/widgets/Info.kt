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
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.foundation.AnimatedVisibilityComposable
import com.varpihovsky.core_ui.compose.foundation.CenterLayoutItem
import com.varpihovsky.repo_data.ExpandButtonLocation

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
@Composable
fun InfoList(
    modifier: Modifier = Modifier,
    title: String,
    buttonLocation: ExpandButtonLocation,
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

        val button: @Composable () -> Unit = {
            ListExpandButton(
                checked = checked,
                moreInfoTitle = moreInfoTitle,
                onToggle = onToggle,
                titlePadding = titlePadding
            )
        }

        val body: @Composable () -> Unit = {
            AnimatedVisibilityComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                visible = checked,
                composable = moreInfoContent
            )
        }

        when (buttonLocation) {
            ExpandButtonLocation.LOWER -> {
                body()
                button()
            }
            ExpandButtonLocation.UPPER -> {
                button()
                body()
            }
        }
    }
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

@Composable
fun SubjectInfo(modifier: Modifier = Modifier, bigText: String, smallText: String) {
    Column(modifier = modifier.padding(start = 12.dp, top = 10.dp)) {
        Text(
            text = bigText,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal)
        )
        Text(text = smallText, style = MaterialTheme.typography.body1)
    }
}