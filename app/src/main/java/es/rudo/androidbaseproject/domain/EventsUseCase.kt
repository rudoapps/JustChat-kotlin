package es.rudo.androidbaseproject.domain

import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import es.rudo.firebasechat.models.results.ResultUserChat
import kotlinx.coroutines.flow.Flow

interface EventsUseCase {
    fun initUser(isNetworkAvailable: Boolean, deviceToken: String): Flow<ResultUserChat>
    fun initCurrentUserChats(isNetworkAvailable: Boolean): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ): Flow<ResultInfo>

    fun getChats(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Chat>>
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
}
