package es.rudo.androidbaseproject.data.repository

import es.rudo.androidbaseproject.data.dto.results.ResultInfo
import es.rudo.androidbaseproject.data.dto.results.ResultUserChat
import es.rudo.androidbaseproject.domain.models.Chat
import es.rudo.androidbaseproject.domain.models.ChatInfo
import es.rudo.androidbaseproject.domain.models.Group
import es.rudo.androidbaseproject.domain.models.Message
import es.rudo.androidbaseproject.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun initUser(isNetworkAvailable: Boolean, deviceToken: String): Flow<ResultUserChat>
    fun initCurrentUserChats(isNetworkAvailable: Boolean): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ): Flow<ResultInfo>

    fun getChats(isNetworkAvailable: Boolean): Flow<MutableList<Chat>>
    fun getMessagesIndividual(
        isNetworkAvailable: Boolean,
        chat: Chat,
        page: Int
    ): Flow<MutableList<Message>>

    fun getCurrentUser(isNetworkAvailable: Boolean): Flow<UserData>
    fun getGroups(isNetworkAvailable: Boolean): Flow<MutableList<Group>>
    fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: Message
    ): Flow<ResultInfo>
}
