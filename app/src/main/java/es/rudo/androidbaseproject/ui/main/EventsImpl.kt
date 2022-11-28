package es.rudo.androidbaseproject.ui.main

import android.content.Context
import es.rudo.androidbaseproject.data.dto.DataNotification
import es.rudo.androidbaseproject.data.dto.Notification
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.androidbaseproject.domain.NotificationsUseCase
import es.rudo.androidbaseproject.helpers.extensions.saveChatId
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsImpl @Inject constructor(
    private val eventsUseCase: EventsUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : Events {
    override fun getChats(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Chat>> {
        return eventsUseCase.getChats(isNetworkAvailable, userId)
    }

    override fun getChatMessages(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>> {
        return eventsUseCase.getChatMessages(isNetworkAvailable, userId, chatId, page)
    }

    override fun getCurrentUser(isNetworkAvailable: Boolean, userId: String): Flow<UserData> {
        return eventsUseCase.getCurrentUser(isNetworkAvailable, userId)
    }

    override fun getGroups(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Group>> {
        return eventsUseCase.getGroups(isNetworkAvailable, userId)
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo> {
        return eventsUseCase.sendMessage(isNetworkAvailable, chatInfo, message)
    }

    override fun initFlowReceiveMessage(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String
    ): Flow<ChatMessageItem> {
        return eventsUseCase.initFlowReceiveMessage(isNetworkAvailable, userId, chatId)
    }

    override suspend fun sendNotification(
        isNetworkAvailable: Boolean,
        userId: String,
        chat: Chat?,
        message: String?
    ) {
        eventsUseCase.getCurrentUser(isNetworkAvailable, userId).collect {
            val dataNotification = DataNotification(
                chatId = chat?.id.toString(),
                chatDestinationUserName = it.userName.toString(),
                chatDestinationUserId = it.userId.toString(),
                chatDestinationUserImage = it.userPhoto.toString(),
                destinationUserDeviceToken = it.userDeviceToken.toString(),
                chatMessage = message.toString()
            )
            //            "e1ZrKOmgTc6AFtgJYxiXVU:APA91bFYH2pZz9M3DrycsO7ko2awfMICnrxN2BRviS-0oBh01OqBXZDz3qZC-v4LOwQQrK6tV3Vcw7GmYAeoi5AX7zNJ5ugHF1K29MeXvOFVF9duBD-wmG8nTygVejjXzSZ7Fbdf7oim",
            // chat?.userDeviceToken.toString(),
            val notification = Notification(
                to = chat?.userDeviceToken.toString(),
                data = dataNotification,
                priority = 10
            )
            val response = notificationsUseCase.sendNotification(notification)
            response
        }
    }

    override fun manageChatId(context: Context, save: Boolean, chatId: String) {
        if (save) {
            context.saveChatId(chatId)
        } else {
            context.saveChatId("")
        }
    }
}
