package com.varpihovsky.core.testCore

import io.mockk.unmockkAll
import org.junit.After

open class JetIQTest {
    @After
    open fun teardown() {
        unmockkAll()
    }
}