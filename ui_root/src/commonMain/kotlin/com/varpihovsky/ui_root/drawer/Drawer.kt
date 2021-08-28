package com.varpihovsky.ui_root.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.varpihovsky.core_ui.compose.widgets.Avatar
import com.varpihovsky.core_ui.compose.widgets.DrawerItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Drawer(drawerComponent: DrawerComponent) {
    val profileLink by drawerComponent.profileLink.collectAsState("")
    val temporalNavigationText by drawerComponent.temporalNavigationText.subscribeAsState()
    val temporalNavigationContent by drawerComponent.temporalNavigationContent.collectAsState()
    Card(modifier = Modifier.fillMaxHeight()) {
        Column {
            Avatar(modifier = Modifier.padding(10.dp), url = profileLink)

            Divider(modifier = Modifier.padding(15.dp))

            AnimatedVisibility(
                visible = temporalNavigationText.isNotEmpty() && temporalNavigationContent != null
            ) {
                Column {
                    Text(text = temporalNavigationText)
                    temporalNavigationContent?.invoke()
                    Divider(modifier = Modifier.padding(15.dp))
                }
            }

            Column {
                DrawerItem(
                    text = "Повідомлення",
                    icon = rememberVectorPainter(Icons.Default.Message),
                    onClick = drawerComponent::onMessagesClick
                )
                DrawerItem(
                    text = "Профіль",
                    icon = rememberVectorPainter(Icons.Default.Person),
                    onClick = drawerComponent::onProfileClick
                )
                DrawerItem(
                    text = "Налаштування",
                    icon = rememberVectorPainter(Icons.Default.Settings),
                    onClick = drawerComponent::onSettingsClick
                )
            }
        }
    }
}