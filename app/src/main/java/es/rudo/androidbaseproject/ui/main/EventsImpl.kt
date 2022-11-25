package es.rudo.androidbaseproject.ui.main

import es.rudo.firebasechat.interfaces.Events
import es.rudo.firebasechat.models.*
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.flow.Flow

class EventsImpl : Events {
    override fun getChats(isNetworkAvailable: Boolean): Flow<MutableList<Chat>>? {
        return MainViewModel.eventsUseCase?.getChats(isNetworkAvailable)
    }

    override fun getMessagesIndividual(
        isNetworkAvailable: Boolean,
        chat: Chat,
        page: Int
    ): Flow<MutableList<ChatMessageItem>>? {
        return MainViewModel.eventsUseCase?.getMessagesIndividual(isNetworkAvailable, chat, page)
    }

    override fun getCurrentUser(isNetworkAvailable: Boolean): Flow<UserData>? {
        return MainViewModel.eventsUseCase?.getCurrentUser(isNetworkAvailable)
    }

    override fun getGroups(isNetworkAvailable: Boolean): Flow<MutableList<Group>>? {
        return MainViewModel.eventsUseCase?.getGroups(isNetworkAvailable)
    }

    override fun sendMessage(
        isNetworkAvailable: Boolean,
        chatInfo: ChatInfo,
        message: ChatMessageItem
    ): Flow<ResultInfo>? {
        return MainViewModel.eventsUseCase?.sendMessage(isNetworkAvailable, chatInfo, message)
    }
}
