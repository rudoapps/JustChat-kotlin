package es.rudo.androidbaseproject.data.source.remote

import es.rudo.justchat.models.*
import es.rudo.justchat.models.results.ResultInfo
import es.rudo.justchat.models.results.ResultUserChat
import kotlinx.coroutines.flow.Flow

interface EventsRemoteDataSource {
    fun initUser(deviceToken: String): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(userId: String): Flow<MutableList<Chat>>
    fun getChat(userId: String, chatId: String): Flow<Chat>
    fun getCurrentUser(userId: String): Flow<UserData>
    fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>

    fun getGroups(userId: String): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: ChatMessageItem): Flow<ResultInfo>
    fun initFlowReceiveMessage(userId: String, chatId: String): Flow<ChatMessageItem>
}
