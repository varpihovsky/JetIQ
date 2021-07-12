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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltWorker
class SessionRestorationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val profileModel: ProfileRepo
) : Worker(context, workerParameters) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var job: Job

    override fun doWork(): Result {

        job = scope.launch {
            profileModel.getConfidential().collect {
                profileModel.logout()
                profileModel.login(it.login, it.password)
                job.cancel()
            }
        }

        return Result.success()
    }


}