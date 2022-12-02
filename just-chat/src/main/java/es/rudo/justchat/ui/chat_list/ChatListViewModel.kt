package es.rudo.justchat.ui.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.justchat.helpers.utils.userId
import es.rudo.justchat.main.instance.JustChat
import es.rudo.justchat.models.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel : ViewModel() {
    val chats = MutableLiveData<MutableList<Chat>>()

    fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            JustChat.events?.initFlowGetChats(userId.toString())?.collect {
                withContext(Dispatchers.Main) {
                    chats.value = it
                }
            }
        }
    }
}
