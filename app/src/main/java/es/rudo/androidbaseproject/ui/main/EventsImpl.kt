package es.rudo.androidbaseproject.ui.main

import android.content.Context
import es.rudo.androidbaseproject.data.dto.DataNotification
import es.rudo.androidbaseproject.data.dto.Notification
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.androidbaseproject.domain.NotificationsUseCase
import es.rudo.androidbaseproject.helpers.extensions.isNetworkAvailable
import es.rudo.androidbaseproject.helpers.extensions.saveChatId
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsImpl @Inject constructor(
    private val context: Context,
    private val eventsUseCase: EventsUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : Events {
    override fun getChats(userId: String): Flow<MutableList<Chat>> {
        return eventsUseCase.getChats(context.isNetworkAvailable, userId)
    }

    override fun getChat(userId: String, chatId: String): Flow<Chat> {
        return eventsUseCase.getChat(context.isNetworkAvailable, userId, chatId)
    }

    override fun getGroups(userId: String): Flow<MutableList<Group>> {
        return eventsUseCase.getGroups(context.isNetworkAvailable, userId)
    }

    override fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>> {
        return eventsUseCase.getChatMessages(context.isNetworkAvailable, userId, chatId, page)
    }

    override fun getCurrentUser(userId: String): Flow<UserData> {
        return eventsUseCase.getCurrentUser(context.isNetworkAvailable, userId)
    }

    override fun sendMessage(
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo> {
        return eventsUseCase.sendMessage(context.isNetworkAvailable, chatInfo, message)
    }

    override fun initFlowReceiveMessage(
        userId: String,
        chatId: String
    ): Flow<ChatMessageItem> {
        return eventsUseCase.initFlowReceiveMessage(context.isNetworkAvailable, userId, chatId)
    }

    override suspend fun sendNotification(
        userId: String,
        chat: Chat?,
        message: String?
    ) {
        eventsUseCase.getCurrentUser(context.isNetworkAvailable, userId).collect {
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
