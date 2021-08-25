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
package com.varpihovsky.core_ui.compose.entities

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.varpihovsky.core_ui.compose.widgets.Avatar
import com.varpihovsky.core_ui.compose.widgets.BoxInfo
import com.varpihovsky.ui_data.dto.UIProfileDTO

@Composable
fun ProfileName(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(bottom = 9.dp),
        text = text,
        style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Start)
    )
}

@Composable
fun StudentInfo(
    profile: UIProfileDTO
) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        BoxInfo(bigText = profile.faculty, smallText = "Факультет")
        BoxInfo(bigText = profile.course.toString(), smallText = "Курс")
        BoxInfo(bigText = profile.groupName, smallText = "Група")
        BoxInfo(bigText = profile.subgroupNumber.toString(), smallText = "Номер підгрупи")
    }
}

@Composable
fun ProfileInfoBar(
    modifier: Modifier = Modifier,
    profile: UIProfileDTO
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Avatar(
            modifier = Modifier
                .padding(horizontal = 9.dp)
                .requiredSize(35.dp),
            url = profile.photoURL
        )
        ProfileName(
            text = profile.name
        )
    }
}
