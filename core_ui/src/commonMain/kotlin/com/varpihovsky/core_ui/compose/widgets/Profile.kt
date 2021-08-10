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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.fileFetcher
import io.kamel.core.config.takeFrom
import io.kamel.core.utils.cacheControl
import io.kamel.image.KamelImage
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.lazyPainterResource
import io.ktor.client.utils.*

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    url: String,
    colorFilter: ColorFilter? = null,
    transformation: Transformation? = Transformation.CircleCropTransformation,
    placeholderEnabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit
) {
    val currentModifier = when (transformation) {
        Transformation.CircleCropTransformation -> modifier.clip(CircleShape)
        null -> modifier
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        KamelImage(
            modifier = currentModifier,
            contentDescription = null,
            contentScale = contentScale,
            colorFilter = colorFilter,
            onLoading = { AvatarPlaceholder(placeholderEnabled) },
            onFailure = { AvatarPlaceholder(placeholderEnabled) },
            resource = lazyPainterResource(url) {
                this.requestBuilder {
                    cacheControl(CacheControl.MAX_AGE)
                }
            }
        )

    }
}

@Composable
private fun AvatarPlaceholder(placeholderEnabled: Boolean) {
    Box(Modifier.fillMaxSize()) {
        if (placeholderEnabled) {
            PersonIcon()
        }
    }
}

sealed class Transformation {
    object CircleCropTransformation : Transformation()
}

private val kamelConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
    fileFetcher()

    imageBitmapCacheSize = 100
    imageVectorCacheSize = 100
}