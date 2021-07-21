package com.varpihovsky.jetiq.services

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