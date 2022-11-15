package es.rudo.firebasechat.domain.impl

import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatInfo
import es.rudo.firebasechat.domain.models.Group
import es.rudo.firebasechat.domain.models.Message
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
