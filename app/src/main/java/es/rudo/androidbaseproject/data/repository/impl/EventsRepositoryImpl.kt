package es.rudo.androidbaseproject.data.repository.impl

import es.rudo.androidbaseproject.data.repository.EventsRepository
import es.rudo.androidbaseproject.data.source.local.EventsLocalDataSource
import es.rudo.androidbaseproject.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import es.rudo.firebasechat.models.results.ResultUserChat
import getResult
import getResultUserChat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsRemoteDataSource: EventsRemoteDataSource,
    private val eventsLocalDataSource: EventsLocalDataSource
) : EventsRepository {

    override fun initUser(isNetworkAvailable: Boolean, deviceToken: String): Flow<ResultUserChat> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.initUser(deviceToken)
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

    override fun getChats(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Chat>> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.getChats(userId)
        } else {
            eventsLocalDataSource.getChats(userId)
        }
    }

    override fun getChat(isNetworkAvailable: Boolean, userId: String, chatId: String): Flow<Chat> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.getChat(userId, chatId)
        } else {
            flow {
                emit(Chat())
            }
        }
    }

    override fun getCurrentUser(isNetworkAvailable: Boolean, userId: String): Flow<UserData> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.getCurrentUser(userId)
        } else {
            flow {
                emit(UserData())
            }
        }
    }

    override fun getChatMessages(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.getChatMessages(userId, chatId, page)
        } else {
            eventsLocalDataSource.getChatMessages(userId, chatId, page)
        }
    }

    override fun getGroups(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Group>> {
        return eventsRemoteDataSource.getGroups(userId)
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.sendMessage(chatInfo, message)
        } else {
            flow {
                emit(getResult(isSuccess = false, exception = Exception("No connection")))
            }
        }
    }

    override fun initFlowReceiveMessage(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String
    ): Flow<ChatMessageItem> {
        return if (isNetworkAvailable) {
            eventsRemoteDataSource.initFlowReceiveMessage(userId, chatId)
        } else {
            flow {
                emit(ChatMessageItem())
            }
        }
    }
}
