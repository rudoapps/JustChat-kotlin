package es.rudo.firebasechat.domain.impl

import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.data.model.result.ResultInfo
import es.rudo.firebasechat.data.model.result.ResultUserChat
import es.rudo.firebasechat.data.repository.EventsRepository
import es.rudo.firebasechat.domain.EventsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsUseCaseImpl @Inject constructor(private val eventsRepository: EventsRepository) :
    EventsUseCase {

    override fun initUser(): Flow<ResultUserChat> {
        return eventsRepository.initUser()
    }

    override fun initCurrentUserChats(): Flow<MutableList<Pair<String, String>>> {
        return eventsRepository.initCurrentUserChats()
    }

    override fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>): Flow<ResultInfo> {
        return eventsRepository.initOtherUsersChats(listChatId)
    }

    override fun getChats(): Flow<MutableList<Chat>> {
        return eventsRepository.getChats()
    }

    override fun getMessagesIndividual(chat: Chat, page: Int): Flow<MutableList<Message>> {
        return eventsRepository.getMessagesIndividual(chat, page)
    }

    override fun getGroups(): Flow<MutableList<Group>> {
        return eventsRepository.getGroups()
    }

    override fun sendMessage(chatInfo: ChatInfo, message: Message): Flow<ResultInfo> {
        return eventsRepository.sendMessage(chatInfo, message)
    }
}
