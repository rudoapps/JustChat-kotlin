package es.rudo.firebasechat.ui.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.NotificationsUseCase
import es.rudo.firebasechat.domain.models.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : ViewModel() {

    val chats = MutableLiveData<MutableList<Chat>>()
    val userInitialized = MutableLiveData<ResultUserChat>()
    val listChatId = MutableLiveData<MutableList<Pair<String, String>>>()
    val chatsInitialized = MutableLiveData<ResultInfo>()

    fun sendNotification(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = notificationsUseCase.sendNotification(notification)
            response
        }
    }

    fun getChats(isNetworkAvailable: Boolean) {
        viewModelScope.launch {
            eventsUseCase.getChats(isNetworkAvailable).collect {
                chats.postValue(it)
            }
        }
    }

    fun initUser(isNetworkAvailable: Boolean) {
        viewModelScope.launch {
            eventsUseCase.initUser(isNetworkAvailable).collect {
                userInitialized.value = it
            }
        }
    }

    fun initCurrentUserChats(isNetworkAvailable: Boolean) {
        viewModelScope.launch {
            eventsUseCase.initCurrentUserChats(isNetworkAvailable).collect {
                listChatId.value = it
            }
        }
    }

    fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ) {
        viewModelScope.launch {
            eventsUseCase.initOtherUsersChats(isNetworkAvailable, listChatId).collect {
                chatsInitialized.value = it
            }
        }
    }
}
