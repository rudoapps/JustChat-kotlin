package es.rudo.firebasechat.data.repository.impl

import android.content.Context
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.data.model.configuration.BasicConfiguration
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.data.source.local.EventsLocalDataSource
import es.rudo.firebasechat.data.source.remote.EventsRemoteDataSource
import es.rudo.firebasechat.helpers.extensions.isNetworkAvailable
import es.rudo.firebasechat.main.instance.RudoChatInstance
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val eventsRemoteDataSource: EventsRemoteDataSource,
    private val eventsLocalDataSource: EventsLocalDataSource
) : EventsRepository {

    override suspend fun initChat(chat: Chat) {
        when (RudoChatInstance.getType()) {
            BasicConfiguration.Type.FIREBASE -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.initChat(chat)
                } else {
                    eventsLocalDataSource.initChat(chat)
                }
            }
            BasicConfiguration.Type.BACK -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.initChat(chat)
                } else {
                    eventsLocalDataSource.initChat(chat)
                }
            }
            BasicConfiguration.Type.MIX -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.initChat(chat)
                } else {
                    eventsLocalDataSource.initChat(chat)
                }
            }
            else -> { // By default return user conf
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.initChat(chat)
                } else {
                    eventsLocalDataSource.initChat(chat)
                }
            }
        }
    }

    override fun getChats(): Flow<MutableList<Chat>> {
        return when (RudoChatInstance.getType()) {
            BasicConfiguration.Type.FIREBASE -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getChats()
                } else {
                    eventsLocalDataSource.getChats()
                }
            }
            BasicConfiguration.Type.BACK -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getChats()
                } else {
                    eventsLocalDataSource.getChats()
                }
            }
            BasicConfiguration.Type.MIX -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getChats()
                } else {
                    eventsLocalDataSource.getChats()
                }
            }
            else -> { // By default return user conf
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getChats()
                } else {
                    eventsLocalDataSource.getChats()
                }
            }
        }
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>> {
        return when (RudoChatInstance.getType()) {
            BasicConfiguration.Type.FIREBASE -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getMessagesIndividual(chat, page)
                } else {
                    eventsLocalDataSource.getMessagesIndividual(chat, page)
                }
            }
            BasicConfiguration.Type.BACK -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getMessagesIndividual(chat, page)
                } else {
                    eventsLocalDataSource.getMessagesIndividual(chat, page)
                }
            }
            BasicConfiguration.Type.MIX -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getMessagesIndividual(chat, page)
                } else {
                    eventsLocalDataSource.getMessagesIndividual(chat, page)
                }
            }
            else -> { // By default return user conf
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.getMessagesIndividual(chat, page)
                } else {
                    eventsLocalDataSource.getMessagesIndividual(chat, page)
                }
            }
        }
    }

    override fun getGroups(): Flow<MutableList<Group>> {
        return eventsRemoteDataSource.getGroups()
    }

    override fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<Boolean> {
        return when (RudoChatInstance.getType()) {
            BasicConfiguration.Type.FIREBASE -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.sendMessage(chatInfo, message)
                } else {
                    eventsLocalDataSource.sendMessage(chatInfo, message)
                }
            }
            BasicConfiguration.Type.BACK -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.sendMessage(chatInfo, message)
                } else {
                    eventsLocalDataSource.sendMessage(chatInfo, message)
                }
            }
            BasicConfiguration.Type.MIX -> {
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.sendMessage(chatInfo, message)
                } else {
                    eventsLocalDataSource.sendMessage(chatInfo, message)
                }
            }
            else -> { // By default return user conf
                if (context.isNetworkAvailable) {
                    eventsRemoteDataSource.sendMessage(chatInfo, message)
                } else {
                    eventsLocalDataSource.sendMessage(chatInfo, message)
                }
            }
        }
    }
}
