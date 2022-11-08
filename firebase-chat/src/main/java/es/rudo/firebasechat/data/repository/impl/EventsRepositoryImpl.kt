package es.rudo.firebasechat.data.repository.impl

import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatInfo
import es.rudo.firebasechat.domain.models.Group
import es.rudo.firebasechat.domain.models.Message
import getResult
import getResultUserChat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsRemoteDataSource: EventsRemoteDataSource,
    private val eventsLocalDataSource: EventsLocalDataSource
) : EventsRepository {

    override fun initUser(isNetworkAvailable: Boolean): Flow<ResultUserChat> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.initUser()
        } else {
            flow {
                emit(getResultUserChat(isSuccess = false, exception = Exception("No connection")))
            }
        }
    }

    override fun initCurrentUserChats(isNetworkAvailable: Boolean): Flow<MutableList<Pair<String, String>>> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.initCurrentUserChats()
        } else {
            flow {
                emit(mutableListOf())
            }
        }
    }

    override fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ): Flow<ResultInfo> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.initOtherUsersChats(listChatId)
        } else {
            flow {
                emit(getResult(isSuccess = false, exception = Exception("No connection")))
            }
        }
    }

    override fun getChats(isNetworkAvailable: Boolean): Flow<MutableList<Chat>> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.getChats()
        } else {
            eventsLocalDataSource.getChats()
        }
    }

    override fun getMessagesIndividual(
        isNetworkAvailable: Boolean,
        chat: Chat,
        page: Int
    ): Flow<MutableList<Message>> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.getMessagesIndividual(chat, page)
        } else {
            eventsLocalDataSource.getMessagesIndividual(chat, page)
        }
    }

    override fun getGroups(isNetworkAvailable: Boolean): Flow<MutableList<Group>> {
        return eventsRemoteDataSource.getGroups()
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: Message
    ): Flow<ResultInfo> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.sendMessage(chatInfo, message)
        } else {
            flow {
                emit(getResult(isSuccess = false, exception = Exception("No connection")))
            }
        }
    }
}
