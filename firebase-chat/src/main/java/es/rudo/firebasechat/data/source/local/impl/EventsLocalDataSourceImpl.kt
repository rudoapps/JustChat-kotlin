package es.rudo.firebasechat.data.source.local.impl

import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.data.model.result.ResultInfo
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventsLocalDataSourceImpl : EventsLocalDataSource {

    override fun initUser(): Flow<ResultInfo> {
        return flow {
            emit(ResultInfo())
        }
    }

    override fun initChat(): Flow<ResultInfo> {
        return flow {
            emit(ResultInfo())
        }
    }

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

    override fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo> {
        return flow {
            emit(ResultInfo())
        }
    }
}
