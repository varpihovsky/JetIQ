package com.varpihovsky.core_repo.testCore

import com.varpihovsky.core.exceptions.ExceptionEventManager
import com.varpihovsky.core_test.CoroutineTest
import io.mockk.mockk

open class RepoTest : CoroutineTest() {
    val exceptionEventManager: ExceptionEventManager = mockk(relaxed = true)
}