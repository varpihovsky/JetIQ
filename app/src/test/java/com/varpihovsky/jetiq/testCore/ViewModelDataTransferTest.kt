package com.varpihovsky.jetiq.testCore

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

import com.varpihovsky.core.dataTransfer.ViewModelData
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

abstract class ViewModelDataTransferTest : ViewModelTest() {
    protected val viewModelDataTransferManager: ViewModelDataTransferManager = mockk(relaxed = true)

    protected val dataTransferStateFlow =
        spyk(MutableStateFlow<ViewModelData<*>?>(null))

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()

        every { viewModelDataTransferManager.getFlowByTag(any()) } returns dataTransferStateFlow
    }
}