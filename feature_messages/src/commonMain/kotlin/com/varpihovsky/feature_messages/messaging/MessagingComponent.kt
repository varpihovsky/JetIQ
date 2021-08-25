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
package com.varpihovsky.feature_messages.messaging

import com.varpihovsky.core_repo.repo.ListRepo
import com.varpihovsky.core_repo.repo.MessagesRepo
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.feature_messages.MessagesComponentContext
import com.varpihovsky.ui_data.mappers.toUIDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class MessagingComponent(
    messagesComponentContext: MessagesComponentContext,
    private val receiverId: Int,
    private val receiverType: Int
) : MessagesComponentContext by messagesComponentContext, KoinComponent {

    val state: Flow<List<Message>> by lazy {
        if (receiverId != -1) {
            messagesRepo.getMessages().map { receivedMessages ->
                receivedMessages
                    .filter { it.idFrom == receiverId.toString() && it.isTeacher == receiverType.toString() }
                    .map { it.toUIDTO() }
                    .map { Message(it.sender, it.message, Message.Type.Others, it.time.toLong()) }
            }.combine(
                messagesRepo.getSentMessages(receiverId, receiverType).map {
                    it.map {
                        Message(profileRepo.getProfileDTO()?.fullName ?: "", it.body, Message.Type.Your, it.time)
                    }
                }) { receivedMessages, sentMessages ->

                val all = receivedMessages + sentMessages

                all.sortedByDescending { it.time }
            }
        } else {
            messagesRepo.getMessages().combine(listRepo.getContacts()) { receivedMessages, contacts ->
                receivedMessages.filter { message -> !contacts.any { it.id.toString() == message.id && it.type == message.isTeacher } }
                    .map { /* We still need time info in the next stage of mapping */Pair(it.toUIDTO(), it) }
                    .map {
                        Message(it.first.sender, it.first.message, Message.Type.Others, it.second.time.toLong())
                    }.sortedByDescending { it.time }
            }
        }
    }

    private val messagesRepo: MessagesRepo by inject()
    private val profileRepo: ProfileRepo by inject()
    private val listRepo: ListRepo by inject()


    data class Message(val title: String, val body: String, val type: Type, val time: Long) {
        fun timeToString(): String {
            return Instant.fromEpochSeconds(time).toLocalDateTime(TimeZone.currentSystemDefault()).let {
                "${it.hour}:${it.minute}:${it.second} ${it.dayOfMonth}.${it.month.number}.${it.year}"
            }
        }

        sealed class Type {
            object Your : Type()
            object Others : Type()
        }
    }
}