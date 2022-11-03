package es.rudo.firebasechat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.ChatInfo
import es.rudo.firebasechat.data.model.chats.Message
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.models.Participant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    var participantList: List<Participant>? = null

    val messageList = mutableListOf<Message>()
    val newMessageText = MutableLiveData<String>()

    val chats = MutableLiveData<MutableList<Chat>>()
    val messages = MutableLiveData<MutableList<Message>>()
    val messageSent = MutableLiveData<Boolean>()

    private val _initialMessageListLoaded = MutableLiveData<Boolean>()
    val initialMessageListLoaded: LiveData<Boolean> = _initialMessageListLoaded

    private val _newMessageAddedToList = MutableLiveData<Boolean>()
    val newMessageAddedToList: LiveData<Boolean> = _newMessageAddedToList

    private val _messageListHistoryUpdateStarted = MutableLiveData<Boolean>()
    val messageListHistoryUpdateStarted: LiveData<Boolean> = _messageListHistoryUpdateStarted

    private val _messageListHistoryUpdateFinished = MutableLiveData<Boolean>()
    val messageListHistoryUpdateFinished: LiveData<Boolean> = _messageListHistoryUpdateFinished

    fun getChats() {
        viewModelScope.launch {
            eventsUseCase.getChats().collect {
                chats.postValue(it)
            }
        }
    }

    fun getMessages(chat: Chat) {
        viewModelScope.launch {
            eventsUseCase.getMessagesIndividual(chat, 0).collect {
                messages.postValue(it)
            }
        }
    }

    fun sendMessage(chatInfo: ChatInfo, message: Message) {
        viewModelScope.launch {
            eventsUseCase.sendMessage(chatInfo, message).collect {
                messageSent.postValue(it)
            }
        }
    }

    fun initUser() {
        viewModelScope.launch {
            eventsUseCase.initUser().collect {
            }
        }
    }

    fun loadMessageList() {
        val list = mutableListOf<Message>()

        for (i in 1..20) {
            list.add(
                Message().apply {
                    userId = if (i % 7 == 0) "2" else "1"
                    text = "ChatMessage $i"
                    timestamp = 23423424
                }
            )
        }

        messageList.addAll(list)
        _initialMessageListLoaded.value = true
    }

    // TODO testing
    fun sendMessage() {
        if (!newMessageText.value.isNullOrBlank()) {
            messageList.add(
                Message().apply {
                    userId = "1"
                    text = newMessageText.value
                    timestamp = 23423432424
                }
            )
            newMessageText.value = null
            _newMessageAddedToList.value = true
        }
    }

    fun receiveMessage() {
        // TODO
    }
}
