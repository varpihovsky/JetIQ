package com.varpihovsky.jetiq.testCore

import com.varpihovsky.jetiq.system.dataTransfer.ViewModelData
import com.varpihovsky.jetiq.system.dataTransfer.ViewModelDataTransferManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

abstract class ViewModelDataTransferTest : ViewModelTest() {
    protected val viewModelDataTransferManager: ViewModelDataTransferManager = mockk(relaxed = true)

    protected val dataTransferStateFlow =
        spyk(MutableStateFlow<ViewModelData<*, *>?>(null))

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        every { viewModelDataTransferManager.getFlowByTag(any()) } returns dataTransferStateFlow
    }
}