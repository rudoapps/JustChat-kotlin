package es.rudo.firebasechat.interfaces

import android.content.Context
import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow

interface Events {
    fun getChats(isNetworkAvailable: Boolean): Flow<MutableList<Chat>>?
    fun getMessagesIndividual(
        isNetworkAvailable: Boolean,
        chat: Chat,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>?

    fun getCurrentUser(isNetworkAvailable: Boolean): Flow<UserData>?
    fun getGroups(isNetworkAvailable: Boolean): Flow<MutableList<Group>>?
    fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo>?

    suspend fun sendNotification(isNetworkAvailable: Boolean, chat: Chat?, message: String?)
    fun manageChatId(context: Context, save: Boolean, chatId: String)
}
