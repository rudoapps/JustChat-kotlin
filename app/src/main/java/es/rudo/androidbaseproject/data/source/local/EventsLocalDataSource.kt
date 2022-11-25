package es.rudo.androidbaseproject.data.source.local

import es.rudo.firebasechat.models.Chat
import es.rudo.firebasechat.models.ChatMessageItem
import es.rudo.firebasechat.models.Group
import kotlinx.coroutines.flow.Flow

interface EventsLocalDataSource {
    fun getChats(): Flow<MutableList<Chat>>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<ChatMessageItem>>
    fun getGroups(): Flow<MutableList<Group>>
}
