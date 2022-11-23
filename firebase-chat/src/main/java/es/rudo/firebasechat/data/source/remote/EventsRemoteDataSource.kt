package es.rudo.firebasechat.data.source.remote

import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat

import es.rudo.firebasechat.domain.models.*
import kotlinx.coroutines.flow.Flow

interface EventsRemoteDataSource {
    fun initUser(deviceToken: String): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(): Flow<MutableList<Chat>>
    fun getCurrentUser(): Flow<UserData>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>>
    fun getGroups(): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: ChatMessageItem): Flow<ResultInfo>
}
