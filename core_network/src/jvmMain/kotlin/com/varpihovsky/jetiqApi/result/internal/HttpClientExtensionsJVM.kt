package com.varpihovsky.jetiqApi.result.internal

import com.varpihovsky.jetiqApi.result.ResultFeature
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

internal actual fun checkForNull(feature: ResultFeature, interception: HttpResponseContainer) {
    // Due to server doesn't throw errors with http status codes, but it shows error
    // with result or similar value in json, while other fields are null, or they aren't present at all,
    // so here we check if these fields are null. The next pipeline phase will wrap it with failure
    // result. Reflection below can have performance impact, especially on android devices, but it's very
    // easy to use for developer and in client code we haven't use ugly try-catch blocks.
    (interception.expectedType.type as KClass<Any>).memberProperties.forEach {
        if (it.get(interception.response) == null) {
            feature.status = HttpStatusCode.Forbidden
        }
    }
}