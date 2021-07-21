package com.varpihovsky.core_network.result

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

import com.varpihovsky.core_network.result.Result
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> Result<T>.isSuccess(): Boolean {
    return this is Result.Success
}

fun <T> Result<T>.asSuccess(): Result.Success<T> {
    return this as Result.Success<T>
}

fun <T> Result<T>.asHttpResponse(): Result.Success.HttpResponse<T> {
    return this as Result.Success.HttpResponse<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is Result.Failure<*>)
    }
    return this is Result.Failure<*>
}

fun <T> Result<T>.asFailure(): Result.Failure<*> {
    return this as Result.Failure<*>
}

fun <T, R> Result<T>.map(transform: (value: T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success.Value(transform(value))
        is Result.Failure<*> -> this
    }
}

fun <T> Result<T>.isEmpty(): Boolean {
    return !isSuccess() && !isFailure()
}

fun <T, R> Result<T>.flatMap(transform: (result: Result<T>) -> Result<R>): Result<R> {
    return transform(this)
}