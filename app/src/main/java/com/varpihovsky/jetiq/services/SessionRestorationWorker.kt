package com.varpihovsky.jetiq.services

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

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.varpihovsky.core_repo.repo.ProfileRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

/**
 * Due to JetIQ server deletes session every 10 hours, we need to restore it.
 * This service reauthorizes on server periodically.
 *
 * @author Vladyslav Podrezenko
 */
@HiltWorker
class SessionRestorationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val profileModel: ProfileRepo
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            reauthorize()
        }

        return Result.success()
    }

    private suspend fun reauthorize() {
        profileModel.getConfidential().last().let {
            profileModel.logout()
            profileModel.login(it.login, it.password)
        }
    }
}