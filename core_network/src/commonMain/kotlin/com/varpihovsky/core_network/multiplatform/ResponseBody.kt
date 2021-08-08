package com.varpihovsky.core_network.multiplatform

interface ResponseBody {
    fun string(): String
}

expect abstract class ResponseBodyImpl : ResponseBody