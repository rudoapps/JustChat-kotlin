package es.rudo.firebasechat.ui.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.domain.EventsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    val chats = MutableLiveData<MutableList<Chat>>()

    fun getChats() {
        viewModelScope.launch {
            eventsUseCase.getChats().collect {
                chats.postValue(it)
            }
        }
    }
}