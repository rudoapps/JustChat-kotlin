package es.rudo.firebasechat.ui.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.firebasechat.helpers.utils.userId
import es.rudo.firebasechat.main.instance.JustChat
import es.rudo.firebasechat.models.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel : ViewModel() {
    val chats = MutableLiveData<MutableList<Chat>>()

    fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            JustChat.events?.getChats(userId.toString())?.collect {
                withContext(Dispatchers.Main) {
                    chats.value = it
                }
            }
        }
    }
}
