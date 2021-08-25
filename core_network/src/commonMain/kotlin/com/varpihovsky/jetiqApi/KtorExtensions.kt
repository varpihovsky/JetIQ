package com.varpihovsky.jetiqApi

import com.varpihovsky.jetiqApi.result.Result
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.get as ktorGet

internal suspend inline fun <reified T : Result<*>> HttpClient.get(
    url: String,
    block: HttpRequestBuilder.() -> Unit
): T = try {
    ktorGet(url, block)
} catch (t: Throwable) {
    Result.of(t)
}