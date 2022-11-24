package es.rudo.androidbaseproject.data.source.local.impl

import es.rudo.androidbaseproject.data.source.local.EventsLocalDataSource
import es.rudo.androidbaseproject.domain.models.Chat
import es.rudo.androidbaseproject.domain.models.Group
import es.rudo.androidbaseproject.domain.models.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventsLocalDataSourceImpl : EventsLocalDataSource {

    override fun getChats(): Flow<MutableList<Chat>> {
        return flow {
            emit(ArrayList())
        }
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>> {
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
