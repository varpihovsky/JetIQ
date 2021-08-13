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
package com.varpihovsky.jetiqApi.config

import com.varpihovsky.jetiqApi.result.ResultFeature
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.builtins.*

class ApiConfigBuilder {
    var httpClient = HttpClient()
    var successfulStatusCodes = listOf(HttpStatusCode.OK, HttpStatusCode.Accepted)

    fun build(): ApiConfig {
        val client = preconfigureClient()
        return ApiConfig(client)
    }

    private fun preconfigureClient() = httpClient.config {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                this.ignoreUnknownKeys = true
            })
            accept(ContentType.Any)
        }
        install(ResultFeature) {
            this.successfulStatusCodes = this@ApiConfigBuilder.successfulStatusCodes
        }
        expectSuccess = false
        HttpResponseValidator {
            validateResponse { }
            handleResponseException { }
        }
    }

}