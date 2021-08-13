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
package com.varpihovsky.jetiqApi.result.internal

import com.varpihovsky.jetiqApi.result.ResultFeature
import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.util.pipeline.*

internal fun HttpClient.nullCheckPhase(feature: ResultFeature) {
    val nullCheckPhase = PipelinePhase("nullCheck")

    responsePipeline.insertPhaseBefore(HttpResponsePipeline.After, nullCheckPhase)

    responsePipeline.intercept(nullCheckPhase) { interception ->
        checkForNull(feature, interception)
        proceed()
    }
}

internal expect fun checkForNull(feature: ResultFeature, interception: HttpResponseContainer)

internal fun HttpClient.preTransformPhase(feature: ResultFeature) {
    val jsonTypeTransform = PipelinePhase("jsonTypeTransform")

    responsePipeline.insertPhaseBefore(HttpResponsePipeline.Transform, jsonTypeTransform)

    responsePipeline.intercept(jsonTypeTransform) { payload ->
        // If requested type isn't result, we skip wrapping with result
        feature.responseWrap = payload.expectedType.reifiedType.toString()
            .contains("com.varpihovsky.jetiqApi.result.Result")
        if (feature.responseWrap) {
            // Prepare incoming data to be deserializable
            proceedWith(feature.serializationMocker.mock(payload))
        } else {
            proceed()
        }
    }
}