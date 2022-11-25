package es.rudo.androidbaseproject.ui.main

import android.content.Context
import es.rudo.androidbaseproject.data.dto.DataNotification
import es.rudo.androidbaseproject.data.dto.Notification
import es.rudo.androidbaseproject.helpers.extensions.saveChatId
import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class EventsImpl : Events {
    override fun getChats(isNetworkAvailable: Boolean): Flow<MutableList<Chat>>? {
        return MainViewModel.eventsUseCase?.getChats(isNetworkAvailable)
    }

    override fun getMessagesIndividual(
        isNetworkAvailable: Boolean,
        chat: Chat,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>? {
        return MainViewModel.eventsUseCase?.getMessagesIndividual(isNetworkAvailable, chat, page)
    }

    override fun getCurrentUser(isNetworkAvailable: Boolean): Flow<UserData>? {
        return MainViewModel.eventsUseCase?.getCurrentUser(isNetworkAvailable)
    }

    override fun getGroups(isNetworkAvailable: Boolean): Flow<MutableList<Group>>? {
        return MainViewModel.eventsUseCase?.getGroups(isNetworkAvailable)
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo>? {
        return MainViewModel.eventsUseCase?.sendMessage(isNetworkAvailable, chatInfo, message)
    }

    override suspend fun sendNotification(
        isNetworkAvailable: Boolean,
        chat: Chat?,
        message: String?
    ) {
        MainViewModel.eventsUseCase?.getCurrentUser(isNetworkAvailable)?.collect {
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
            val response = MainViewModel.notificationsUseCase?.sendNotification(notification)
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
