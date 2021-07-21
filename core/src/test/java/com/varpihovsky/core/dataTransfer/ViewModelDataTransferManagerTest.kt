package com.varpihovsky.core.dataTransfer

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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