package com.varpihovsky.core_network.multiplatform

import kotlin.jvm.JvmName

interface Headers {
    operator fun get(name: String): String?

    /** Returns the number of field values. */
    @get:JvmName("size")
    val size: Int

    /** Returns the field at `position`. */
    fun name(index: Int): String

    /** Returns the value at `index`. */
    fun value(index: Int): String

    /** Returns an immutable case-insensitive set of header names. */
    fun names(): Set<String>

    /** Returns an immutable list of the header values for `name`. */
    fun values(name: String): List<String>

    /**
     * Returns the number of bytes required to encode these headers using HTTP/1.1. This is also the
     * approximate size of HTTP/2 headers before they are compressed with HPACK. This value is
     * intended to be used as a metric: smaller headers are more efficient to encode and transmit.
     */
    fun byteCount(): Long

    fun toMultimap(): Map<String, List<String>>
}