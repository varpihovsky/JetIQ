package com.varpihovsky.jetiq.system.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.varpihovsky.jetiq.back.model.ProfileModel
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltWorker
class SessionRestorationWorker @AssistedInject constructor(
    context: Context,
    workerParameters: WorkerParameters,
    private val profileModel: ProfileModel
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