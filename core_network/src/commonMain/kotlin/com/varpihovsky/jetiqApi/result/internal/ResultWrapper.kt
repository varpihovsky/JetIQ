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

import com.varpihovsky.jetiqApi.result.HttpException
import com.varpihovsky.jetiqApi.result.Result
import com.varpihovsky.jetiqApi.result.ResultFeature
import io.ktor.client.call.*
import io.ktor.client.statement.*

internal class ResultWrapper(private val feature: ResultFeature) {
    fun wrap(interception: HttpResponseContainer) = HttpResponseContainer(
        expectedType = typeInfo<Result<Any>>(), if (feature.isSuccessful()) {
            Result.Success.HttpResponse(
                value = interception.response,
                statusCode = feature.status!!,
                url = feature.url!!,
                headers = feature.headers!!
            )
        } else {
            Result.Failure.HttpError(
                HttpException(
                    statusCode = feature.status!!,
                    url = feature.url,
                    cause = null
                )
            )
        }
    )
}