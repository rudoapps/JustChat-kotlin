package es.rudo.androidbaseproject.domain.impl

import es.rudo.androidbaseproject.data.repository.EventsRepository
import es.rudo.androidbaseproject.domain.EventsUseCase
import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import es.rudo.firebasechat.models.results.ResultUserChat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsUseCaseImpl @Inject constructor(private val eventsRepository: EventsRepository) :
    EventsUseCase {

    override fun initUser(isNetworkAvailable: Boolean, deviceToken: String): Flow<ResultUserChat> {
        return eventsRepository.initUser(isNetworkAvailable, deviceToken)
    }

    override fun initCurrentUserChats(isNetworkAvailable: Boolean): Flow<MutableList<Pair<String, String>>> {
        return eventsRepository.initCurrentUserChats(isNetworkAvailable)
    }

    override fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ): Flow<ResultInfo> {
        return eventsRepository.initOtherUsersChats(isNetworkAvailable, listChatId)
    }

    override fun getChats(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Chat>> {
        return eventsRepository.getChats(isNetworkAvailable, userId)
    }

    override fun getChat(isNetworkAvailable: Boolean, userId: String, chatId: String): Flow<Chat> {
        return eventsRepository.getChat(isNetworkAvailable, userId, chatId)
    }

    override fun getChatMessages(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String,
        page: Int
    ): Flow<MutableList<ChatMessageItem>> {
        return eventsRepository.getChatMessages(isNetworkAvailable, userId, chatId, page)
    }

    override fun getCurrentUser(isNetworkAvailable: Boolean, userId: String): Flow<UserData> {
        return eventsRepository.getCurrentUser(isNetworkAvailable, userId)
    }

    override fun getGroups(isNetworkAvailable: Boolean, userId: String): Flow<MutableList<Group>> {
        return eventsRepository.getGroups(isNetworkAvailable, userId)
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo> {
        return eventsRepository.sendMessage(isNetworkAvailable, chatInfo, message)
    }

    override fun initFlowReceiveMessage(
        isNetworkAvailable: Boolean,
        userId: String,
        chatId: String
    ): Flow<ChatMessageItem> {
        return eventsRepository.initFlowReceiveMessage(isNetworkAvailable, userId, chatId)
    }
}
