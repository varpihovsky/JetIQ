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
import com.varpihovsky.ui_data.dto.UIMessageDTO
import com.varpihovsky.ui_data.mappers.toUIDTO
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

/**
 * Background service which notifies user about new messages.
 *
 * @author Vladyslav Podrezenko
 */
@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val messagesModel: MessagesRepo
) : Worker(context, workerParameters) {
    private lateinit var currentMessages: List<MessageDTO>

    /**
     * Creates notification channel if required.
     * Notifies user about message if it isn't in database.
     */
    override fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        CoroutineScope(Dispatchers.IO).launch {
            handleMessages()
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
        currentMessages = messagesModel.getMessages().last()

        messagesModel.loadMessages()
        messagesModel.getMessages().last().let { notifyNewMessages(it) }
    }

    private fun notifyNewMessages(newMessages: List<MessageDTO>) {
        val builder = createBuilder()
        newMessages
            .filter { !currentMessages.contains(it) }
            .map { it.toUIDTO() }
            .forEach { createNotification(builder, it) }
    }

    private fun createBuilder() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(context, CHANNEL_ID)
    } else {
        Notification.Builder(context)
    }

    private fun createNotification(builder: Notification.Builder, message: UIMessageDTO) =
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText("${message.sender}: ${message.message}")
            .let {
                NotificationManagerCompat.from(context).notify(message.id, it.build())
            }


    companion object {
        private const val CHANNEL_ID = "channel_foreground"
        private const val NOTIFICATION_CHANNEL_NAME = "JetIQ"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION =
            "Нотифікації про надходження нових повідомлень"
        private const val NOTIFICATION_TITLE = "JetIQ - Нове повідомлення!"
    }
}