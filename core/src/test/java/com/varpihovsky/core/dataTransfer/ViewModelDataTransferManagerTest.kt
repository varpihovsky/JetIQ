package com.varpihovsky.core.dataTransfer

import com.varpihovsky.core.testCore.JetIQTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
import org.junit.Before
import org.junit.Test

class ViewModelDataTransferManagerTest : JetIQTest() {
    lateinit var dataTransferManager: ViewModelDataTransferManager

    @Before
    fun setup() {
        dataTransferManager = ViewModelDataTransferManager()
    }

    @Test
    fun `Test same flow returned if same tags provided`() {
        assertEquals(
            dataTransferManager.getFlowByTag(TAG_1),
            dataTransferManager.getFlowByTag(TAG_1)
        )
    }

    @Test
    fun `Test different flows returned if different tags provided`() {
        assertNotSame(
            dataTransferManager.getFlowByTag(TAG_1),
            dataTransferManager.getFlowByTag(TAG_2)
        )
    }

    companion object {
        private const val TAG_1 = "example"
        private const val TAG_2 = "example_1"
    }
}