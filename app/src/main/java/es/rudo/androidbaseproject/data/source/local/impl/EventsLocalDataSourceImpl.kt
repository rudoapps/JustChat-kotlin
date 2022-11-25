package es.rudo.androidbaseproject.data.source.local.impl

import es.rudo.androidbaseproject.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.models.Chat
import es.rudo.firebasechat.models.ChatMessageItem
import es.rudo.firebasechat.models.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventsLocalDataSourceImpl : EventsLocalDataSource {

    override fun getChats(): Flow<MutableList<Chat>> {
        return flow {
            emit(ArrayList())
        }
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<ChatMessageItem>> {
        return flow {
            emit(ArrayList())
        }
    }

    override fun getGroups(): Flow<MutableList<Group>> {
        return flow {
            emit(ArrayList())
        }
    }
}
