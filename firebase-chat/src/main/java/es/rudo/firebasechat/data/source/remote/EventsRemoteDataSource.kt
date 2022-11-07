package es.rudo.firebasechat.data.source.remote

import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.data.model.result.ResultInfo
import es.rudo.firebasechat.data.model.result.ResultUserChat
import kotlinx.coroutines.flow.Flow

interface EventsRemoteDataSource {
    fun initUser(): Flow<ResultUserChat>
    fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>>
    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo>
    fun getChats(): Flow<MutableList<Chat>>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>>
    fun getGroups(): Flow<MutableList<Group>>
    fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo>
}
