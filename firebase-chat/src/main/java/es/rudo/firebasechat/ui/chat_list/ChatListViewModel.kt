package es.rudo.firebasechat.ui.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.main.instance.RudoChatInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    val chats = MutableLiveData<MutableList<Chat>>()
    val userInitialized = MutableLiveData<ResultUserChat>()
    val listChatId = MutableLiveData<MutableList<Pair<String, String>>>()
    val chatsInitialized = MutableLiveData<ResultInfo>()

    fun sendNotification(notification: Notification) {
        viewModelScope.launch {
            val response = eventsUseCase.sendNotification(
                RudoChatInstance.getFirebaseAuth()?.uid.toString(),
                notification
            )
            response
        }
    }

    fun getChats() {
        viewModelScope.launch {
            eventsUseCase.getChats().collect {
                chats.postValue(it)
            }
        }
    }

    fun initUser() {
        viewModelScope.launch {
            eventsUseCase.initUser().collect {
                userInitialized.value = it
            }
        }
    }

    fun initCurrentUserChats() {
        viewModelScope.launch {
            eventsUseCase.initCurrentUserChats().collect {
                listChatId.value = it
            }
        }
    }

    fun initOtherUsersChats(listChatId: MutableList<Pair<String, String>>) {
        viewModelScope.launch {
            eventsUseCase.initOtherUsersChats(listChatId).collect {
                chatsInitialized.value = it
            }
        }
    }
}
