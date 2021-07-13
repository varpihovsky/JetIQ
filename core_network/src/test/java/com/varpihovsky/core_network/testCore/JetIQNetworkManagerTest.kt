package com.varpihovsky.core_network.testCore

import com.varpihovsky.core_network.JetIQApi
import com.varpihovsky.core_network.result.Result
import io.mockk.mockk
import okhttp3.ResponseBody

open class JetIQNetworkManagerTest : CoroutineTest() {
    protected val jetIQApi: JetIQApi = mockk(relaxed = true)
    protected val responseBody: ResponseBody = mockk(relaxed = true)

    companion object {
        val RESULT_FAILURE = Result.Failure.Error(RuntimeException())
    }
}