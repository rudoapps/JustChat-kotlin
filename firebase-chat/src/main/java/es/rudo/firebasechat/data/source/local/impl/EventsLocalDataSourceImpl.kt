package es.rudo.firebasechat.data.source.local.impl

import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.Group
import es.rudo.firebasechat.domain.models.Message
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
