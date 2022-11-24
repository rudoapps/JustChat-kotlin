package es.rudo.androidbaseproject.data.source.local

import es.rudo.androidbaseproject.domain.models.Chat
import es.rudo.androidbaseproject.domain.models.Group
import es.rudo.androidbaseproject.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface EventsLocalDataSource {
    fun getChats(): Flow<MutableList<Chat>>
    fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>>
    fun getGroups(): Flow<MutableList<Group>>
}
