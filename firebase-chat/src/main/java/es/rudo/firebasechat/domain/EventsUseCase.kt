package es.rudo.firebasechat.domain

import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatInfo
import es.rudo.firebasechat.domain.models.Group
import es.rudo.firebasechat.domain.models.Message
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface EventsUseCase {
    suspend fun sendNotification(userId: String, notification: Notification): Response<Void>
    fun initUser(): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(): Flow<MutableList<Chat>>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>>
    fun getGroups(): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo>
}
