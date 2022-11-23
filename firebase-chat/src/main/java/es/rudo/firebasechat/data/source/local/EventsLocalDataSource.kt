package es.rudo.firebasechat.data.source.local

import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatMessageItem
import es.rudo.firebasechat.domain.models.Group
import kotlinx.coroutines.flow.Flow

interface EventsLocalDataSource {
    fun getChats(): Flow<MutableList<Chat>>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<ChatMessageItem>>
    fun getGroups(): Flow<MutableList<Group>>
}
