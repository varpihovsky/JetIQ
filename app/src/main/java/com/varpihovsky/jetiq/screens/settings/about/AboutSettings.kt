package com.varpihovsky.jetiq.screens.settings.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.varpihovsky.jetiq.screens.settings.SettingsButton
import com.varpihovsky.jetiq.ui.compose.BasicAppBar

@Composable
fun AboutSettingsScreen(
    aboutSettingsViewModel: AboutSettingsViewModel
) {
    aboutSettingsViewModel.assignAppbar {
        BasicAppBar(
            title = "Про додаток",
            onBackClick = aboutSettingsViewModel::onBackNavButtonClick
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "JetIQ",
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 10.dp),
            text = "Неофіційний клієнт для JetIQ.\n\nСтабільний, гнучкий та красивий!",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
        SettingsButton(
            title = "Розробник",
            hint = "Подрезенко Владислав",
            onClick = {}
        )
        SettingsButton(
            title = "GitHub",
            hint = "github.com/varpihovsky",
            onClick = {}
        )
        SettingsButton(
            title = "LinkedIn",
            hint = "linkedin.com/in/varpihovsky",
            onClick = {}
        )
    }
}