package es.rudo.androidbaseproject.data.source.remote

import es.rudo.androidbaseproject.domain.models.Chat
import es.rudo.androidbaseproject.domain.models.ChatInfo
import es.rudo.androidbaseproject.domain.models.Group
import es.rudo.androidbaseproject.domain.models.Message
import es.rudo.androidbaseproject.domain.models.UserData
import es.rudo.androidbaseproject.data.dto.results.ResultInfo
import es.rudo.androidbaseproject.data.dto.results.ResultUserChat
import kotlinx.coroutines.flow.Flow

interface EventsRemoteDataSource {
    fun initUser(deviceToken: String): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(): Flow<MutableList<Chat>>
    fun getCurrentUser(): Flow<UserData>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>>
    fun getGroups(): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo>
}
