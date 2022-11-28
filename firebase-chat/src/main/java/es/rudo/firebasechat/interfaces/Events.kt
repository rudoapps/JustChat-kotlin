package es.rudo.firebasechat.interfaces

import android.content.Context
import es.rudo.firebasechat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow

interface Events {
    suspend fun getChats(userId: String): Flow<MutableList<Chat>>
    suspend fun getChat(userId: String, chatId: String): Flow<Chat>
    suspend fun getGroups(userId: String): Flow<MutableList<Group>>
    suspend fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>

    suspend fun getCurrentUser(userId: String): Flow<UserData>
    suspend fun sendMessage(chatInfo: ChatInfo, message: ChatMessageItem): Flow<ResultInfo>
    suspend fun initFlowReceiveMessage(userId: String, chatId: String): Flow<ChatMessageItem>
    suspend fun sendNotification(userId: String, chat: Chat?, message: String?)
    fun manageChatId(context: Context, save: Boolean, chatId: String)
}
