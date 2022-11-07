package es.rudo.firebasechat.data.source.local

import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatInfo
import es.rudo.firebasechat.domain.models.Group
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import kotlinx.coroutines.flow.Flow

interface EventsLocalDataSource {
    fun initUser(): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(): Flow<MutableList<Chat>>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>>
    fun getGroups(): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo>
}
