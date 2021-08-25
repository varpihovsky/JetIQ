package com.varpihovsky.jetiqApi.result

import com.varpihovsky.jetiqApi.result.internal.ResultWrapper
import com.varpihovsky.jetiqApi.result.internal.SerializationMocker
import com.varpihovsky.jetiqApi.result.internal.nullCheckPhase
import com.varpihovsky.jetiqApi.result.internal.preTransformPhase
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.statement.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.util.*


internal class ResultFeature(private val config: Config) {
    private val resultWrapper = ResultWrapper(this)

    internal val serializationMocker = SerializationMocker()

    internal var responseWrap = true

    internal var status: HttpStatusCode? = null

    internal var headers: Headers? = null

    internal var url: String? = null

    fun fetchRawResponseData(interception: HttpResponse) {
        status = interception.status
        headers = interception.headers
        url = interception.call.request.url.encodedPath
    }

    class Config {
        var successfulStatusCodes: List<HttpStatusCode> =
            listOf(HttpStatusCode.OK, HttpStatusCode.Accepted)
    }

    internal fun isSuccessful(): Boolean = config.successfulStatusCodes.contains(status)


    private fun wrapResponse(interception: HttpResponseContainer): HttpResponseContainer {
        return if (responseWrap) {
            resultWrapper.wrap(interception)
        } else {
            interception
        }
    }

    companion object Feature : HttpClientFeature<Config, ResultFeature> {
        override val key: AttributeKey<ResultFeature> = AttributeKey("ResultInterceptor")

        override fun install(
            feature: ResultFeature, scope: HttpClient
        ) {
            scope.nullCheckPhase(feature)

            scope.preTransformPhase(feature)

            scope.receivePipeline.intercept(HttpReceivePipeline.Before) { interception ->
                feature.fetchRawResponseData(interception)
                proceed()
            }
            scope.responsePipeline.intercept(HttpResponsePipeline.After) { interception ->
                proceedWith(feature.wrapResponse(interception))
            }
        }

        override fun prepare(block: Config.() -> Unit): ResultFeature {
            return ResultFeature(Config().apply(block))
        }

    }
}