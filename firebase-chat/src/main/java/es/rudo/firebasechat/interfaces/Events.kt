package es.rudo.firebasechat.interfaces

import es.rudo.firebasechat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow

interface Events {
    fun getChats(userId: String): Flow<MutableList<Chat>>
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

    // TODO: will be unavailable in the future
    suspend fun sendNotification(userId: String, chat: Chat?, message: String?)
}
