package es.rudo.firebasechat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatInfo
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.main.instance.RudoChatInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    var chat: Chat? = null

    val newMessageText = MutableLiveData<String>()

    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> = _messageList

    private val _sendMessageAttempt = MutableLiveData<Boolean>()
    val sendMessageAttempt: LiveData<Boolean> = _sendMessageAttempt

    private val _sendMessageSuccess = MutableLiveData<ResultInfo>()
    val sendMessageSuccess: LiveData<ResultInfo> = _sendMessageSuccess

    private val _initialMessageListLoaded = MutableLiveData<Boolean>()
    val initialMessageListLoaded: LiveData<Boolean> = _initialMessageListLoaded

    private val _messageListHistoryUpdateStarted = MutableLiveData<Boolean>()
    val messageListHistoryUpdateStarted: LiveData<Boolean> = _messageListHistoryUpdateStarted

    private val _messageListHistoryUpdateFinished = MutableLiveData<Boolean>()
    val messageListHistoryUpdateFinished: LiveData<Boolean> = _messageListHistoryUpdateFinished

    fun getMessages() {
        viewModelScope.launch {
            chat?.let {
                eventsUseCase.getMessagesIndividual(it, 0).collect { messages ->
                    _messageList.postValue(messages)
                }
            }
        }
    }

    fun prepareMessageForSending() {
        viewModelScope.launch {
            RudoChatInstance.getFirebaseAuth()?.uid?.let { userId ->
                if (!newMessageText.value.isNullOrBlank() && chat != null) {
                    val message = Message()
                    message.userId = userId
                    message.text = newMessageText.value

                    val chatInfo = ChatInfo()
                    chatInfo.chatId = chat?.id
                    chatInfo.userId = userId
                    chatInfo.otherUserId = chat?.otherUserId

                    newMessageText.value = ""
                    _messageList.value?.add(message)
                    _messageList.value = _messageList.value
                    _sendMessageAttempt.value = true

                    eventsUseCase.sendMessage(chatInfo, message).collect {
                        _sendMessageSuccess.postValue(it)
                    }
//                    sendMessage(message, chatInfo)
                }
            }
        }
    }

    private suspend fun sendMessage(message: Message, chatInfo: ChatInfo) {
        eventsUseCase.sendMessage(chatInfo, message).collect {
            _sendMessageSuccess.postValue(it)
        }
    }
}
