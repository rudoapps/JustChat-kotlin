package es.rudo.firebasechat.data.repository.impl

import android.content.Context
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.data.model.result.ResultInfo
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.helpers.extensions.isNetworkAvailable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val eventsRemoteDataSource: EventsRemoteDataSource,
    private val eventsLocalDataSource: EventsLocalDataSource
) : EventsRepository {

    override fun initUser(): Flow<ResultInfo> {
        return if (context.isNetworkAvailable) {
            eventsRemoteDataSource.initUser()
        } else {
            eventsLocalDataSource.initUser()
        }
    }

    override fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>> {
        return if (context.isNetworkAvailable) {
            eventsRemoteDataSource.initCurrentUserChats()
        } else {
            eventsLocalDataSource.initCurrentUserChats()
        }
    }

    override fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo> {
        return if (context.isNetworkAvailable) {
            eventsRemoteDataSource.initOtherUsersChats(listChatId)
        } else {
            eventsLocalDataSource.initOtherUsersChats(listChatId)
        }
    }

    override fun getChats(): Flow<MutableList<Chat>> {
        return if (context.isNetworkAvailable) {
            eventsRemoteDataSource.getChats()
        } else {
            eventsLocalDataSource.getChats()
        }
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>> {
        return if (context.isNetworkAvailable) {
            eventsRemoteDataSource.getMessagesIndividual(chat, page)
        } else {
            eventsLocalDataSource.getMessagesIndividual(chat, page)
        }
    }

    override fun getGroups(): Flow<MutableList<Group>> {
        return eventsRemoteDataSource.getGroups()
    }

    override fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo> {
        return if (context.isNetworkAvailable) {
            eventsRemoteDataSource.sendMessage(chatInfo, message)
        } else {
            eventsLocalDataSource.sendMessage(chatInfo, message)
        }
    }
}
