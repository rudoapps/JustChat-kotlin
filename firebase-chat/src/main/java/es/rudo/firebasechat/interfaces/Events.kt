package es.rudo.firebasechat.interfaces

import android.content.Context
import es.rudo.firebasechat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow

interface Events {
    fun getChats(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Chat>>
    fun getChat(isNetworkAvailable: Boolean, userId: String, chatId: String): Flow<Chat>
    fun getChatMessages(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>

    fun getCurrentUser(isNetworkAvailable: Boolean, userId: String): Flow<UserData>
    fun getGroups(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Group>>
    fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo>

    fun initFlowReceiveMessage(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String
    ): Flow<ChatMessageItem>

    suspend fun sendNotification(
        isNetworkAvailable: Boolean,
        userId: String,
        chat: Chat?,
        message: String?
    )

    fun manageChatId(context: Context, save: Boolean, chatId: String)
}
