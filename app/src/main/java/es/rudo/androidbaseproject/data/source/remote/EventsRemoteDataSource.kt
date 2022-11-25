package es.rudo.androidbaseproject.data.source.remote

import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import es.rudo.firebasechat.models.results.ResultUserChat
import kotlinx.coroutines.flow.Flow

interface EventsRemoteDataSource {
    fun initUser(deviceToken: String): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(): Flow<MutableList<Chat>>
    fun getCurrentUser(): Flow<UserData>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<ChatMessageItem>>
    fun getGroups(): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: ChatMessageItem): Flow<ResultInfo>
}
