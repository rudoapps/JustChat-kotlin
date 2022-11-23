package es.rudo.firebasechat.ui.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.models.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    val chats = MutableLiveData<MutableList<Chat>>()
    val userInitialized = MutableLiveData<ResultUserChat>()
    val listChatId = MutableLiveData<MutableList<Pair<String, String>>>()
    val chatsInitialized = MutableLiveData<ResultInfo>()

    fun getChats(isNetworkAvailable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.getChats(isNetworkAvailable).collect {
                withContext(Dispatchers.Main) {
                    chats.postValue(it)
                }
            }
        }
    }

    fun initUser(isNetworkAvailable: Boolean, deviceToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.initUser(isNetworkAvailable, deviceToken).collect {
                withContext(Dispatchers.Main) {
                    userInitialized.value = it
                }
            }
        }
    }

    fun initCurrentUserChats(isNetworkAvailable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.initCurrentUserChats(isNetworkAvailable).collect {
                withContext(Dispatchers.Main) {
                    listChatId.value = it
                }
            }
        }
    }

    fun initOtherUsersChats(
        isNetworkAvailable: Boolean,
        listChatId: MutableList<Pair<String, String>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.initOtherUsersChats(isNetworkAvailable, listChatId).collect {
                withContext(Dispatchers.Main) {
                    chatsInitialized.value = it
                }
            }
        }
    }
}
