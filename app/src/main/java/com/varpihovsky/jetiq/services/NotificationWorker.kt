package com.varpihovsky.jetiq.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.jetiq.R
import com.varpihovsky.repo_data.MessageDTO
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val messagesModel: MessagesRepo
) : Worker(context, workerParameters) {
    private var currentMessages: List<MessageDTO> = listOf()
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var job: Job

    override fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        runBlocking {
            job = scope.launch { handleMessages() }
            job.join()
        }

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION
            }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private suspend fun handleMessages() {
        messagesModel.getMessages().collect {
            if (currentMessages.isEmpty()) {
                currentMessages = it
            } else {
                notifyNewMessages(it)
            }
        }
    }

    private fun notifyNewMessages(newMessages: List<MessageDTO>) {
        val builder = initBuilder()
        newMessages.filter { !currentMessages.contains(it) }
            .forEach { createNotification(builder, it) }
        job.cancel()
    }

    private fun initBuilder() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(context, CHANNEL_ID)
    } else {
        Notification.Builder(context)
    }

    private fun createNotification(builder: Notification.Builder, messageDTO: MessageDTO) =
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText("${messageDTO.body}")
            .let {
                NotificationManagerCompat.from(context)
                    .notify(messageDTO.msg_id.toInt(), it.build())
            }


    companion object {
        private const val CHANNEL_ID = "channel_foreground"
        private const val NOTIFICATION_CHANNEL_NAME = "JetIQ"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION =
            "Нотифікації про надходження нових повідомлень"
        private const val NOTIFICATION_TITLE = "JetIQ - Нове повідомлення!"
    }
}