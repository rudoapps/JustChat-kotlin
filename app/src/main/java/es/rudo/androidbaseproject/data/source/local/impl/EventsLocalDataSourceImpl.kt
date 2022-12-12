package es.rudo.androidbaseproject.data.source.local.impl

import es.rudo.androidbaseproject.data.source.local.EventsLocalDataSource
import es.rudo.justchat.models.Chat
import es.rudo.justchat.models.ChatMessageItem
import es.rudo.justchat.models.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventsLocalDataSourceImpl : EventsLocalDataSource {

    override fun getChats(userId: String): Flow<MutableList<Chat>> {
        return flow {
            emit(ArrayList())
        }
    }

    override fun getChatMessages(
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>> {
        return flow {
            emit(ArrayList())
        }
    }

    override fun getGroups(userId: String): Flow<MutableList<Group>> {
        return flow {
            emit(ArrayList())
        }
    }
}
