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
package com.varpihovsky.jetiqApi.result

import io.ktor.http.*

sealed class Result<out T> {

    sealed class Success<T> : Result<T>() {

        abstract val value: T

        override fun toString() = "Success($value)"

        class Value<T>(override val value: T) : Success<T>()

        data class HttpResponse<T>(
            override val value: T,
            override val statusCode: HttpStatusCode,
            override val url: String? = null,
            override val headers: Headers,
        ) : Success<T>(), com.varpihovsky.jetiqApi.result.HttpResponse

        object Empty : Success<Nothing>() {

            override val value: Nothing get() = error("No value")

            override fun toString() = "Success"
        }
    }


    sealed class Failure<E : Throwable>(open val error: E? = null) : Result<Nothing>() {

        override fun toString() = "Failure($error)"

        class Error(override val error: Throwable) : Failure<Throwable>(error)

        class HttpError(override val error: HttpException) : Failure<HttpException>(),
            HttpResponse {

            override val statusCode: HttpStatusCode get() = error.statusCode

            override val url: String? get() = error.url

            override val headers: Headers? get() = null
        }
    }

    companion object {
        fun <T> success(value: T) = Result.Success.Value(value)

        inline fun <reified T : Result<*>> of(throwable: Throwable) =
            Result.Failure.Error(throwable) as T
    }
}

typealias EmptyResult = Result<Nothing>