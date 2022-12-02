package es.rudo.justchat.interfaces

import es.rudo.justchat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.justchat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow

interface Events {
    fun initFlowGetChats(userId: String): Flow<MutableList<Chat>>
    fun getChat(userId: String, chatId: String): Flow<Chat>
    fun getGroups(userId: String): Flow<MutableList<Group>>
    fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>

    fun getCurrentUser(userId: String): Flow<UserData>
    fun sendMessage(chatInfo: ChatInfo, message: ChatMessageItem): Flow<ResultInfo>
    fun initFlowReceiveMessage(userId: String, chatId: String): Flow<ChatMessageItem>

    /**
     * @param userId current logged user id
     * @param chat current chat which you want to send notification
     * @param message message sended to display in the notification
     *
     * It's mandatory to pass all parameters to be able to open the notification.
     * A call to obtain user data, is required in order to give the destination user the correct info.
     * To get more information, see the implementation example in app module.
     */
    suspend fun sendNotification(userId: String, chat: Chat?, message: String?)
}
