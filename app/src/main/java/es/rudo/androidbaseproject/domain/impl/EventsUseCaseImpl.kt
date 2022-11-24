package es.rudo.androidbaseproject.domain.impl

import es.rudo.androidbaseproject.data.repository.EventsRepository
import es.rudo.androidbaseproject.domain.models.Chat
import es.rudo.androidbaseproject.domain.models.ChatInfo
import es.rudo.androidbaseproject.domain.models.Group
import es.rudo.androidbaseproject.domain.models.Message
import es.rudo.androidbaseproject.domain.models.UserData
import es.rudo.androidbaseproject.data.dto.results.ResultInfo
import es.rudo.androidbaseproject.data.dto.results.ResultUserChat
import es.rudo.androidbaseproject.domain.EventsUseCase
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

    override fun getChats(isNetworkAvailable: Boolean): Flow<MutableList<Chat>> {
        return eventsRepository.getChats(isNetworkAvailable)
    }

    override fun getMessagesIndividual(
        isNetworkAvailable: Boolean,
        chat: Chat,
        page: Int
    ): Flow<MutableList<Message>> {
        return eventsRepository.getMessagesIndividual(isNetworkAvailable, chat, page)
    }

    override fun getCurrentUser(isNetworkAvailable: Boolean): Flow<UserData> {
        return eventsRepository.getCurrentUser(isNetworkAvailable)
    }

    override fun getGroups(isNetworkAvailable: Boolean): Flow<MutableList<Group>> {
        return eventsRepository.getGroups(isNetworkAvailable)
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: Message
    ): Flow<ResultInfo> {
        return eventsRepository.sendMessage(isNetworkAvailable, chatInfo, message)
    }
}
