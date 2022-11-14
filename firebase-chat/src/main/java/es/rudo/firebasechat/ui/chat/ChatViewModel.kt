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

    private val _newMessageReceived = MutableLiveData<Boolean>()
    val newMessageReceived: LiveData<Boolean> = _newMessageReceived

    private val _sendMessageAttempt = MutableLiveData<Boolean>()
    val sendMessageAttempt: LiveData<Boolean> = _sendMessageAttempt

    private val _sendMessageSuccess = MutableLiveData<ResultInfo>()
    val sendMessageSuccess: LiveData<ResultInfo> = _sendMessageSuccess

    private val _messageListHistoryUpdateStarted = MutableLiveData<Boolean>()
    val messageListHistoryUpdateStarted: LiveData<Boolean> = _messageListHistoryUpdateStarted

    private val _messageListHistoryUpdateFinished = MutableLiveData<Boolean>()
    val messageListHistoryUpdateFinished: LiveData<Boolean> = _messageListHistoryUpdateFinished

    //TODO esto irá separado en getMessageHistory y getNewMessage
    private var firstLoad = true
    fun getMessages(initialMessageList: MutableList<Message>?) {
        _messageList.value = initialMessageList ?: mutableListOf()

        viewModelScope.launch {
            chat?.let {
                eventsUseCase.getMessagesIndividual(it, 0).collect { messages ->
                    if (firstLoad) {
                        _messageList.postValue(messages)
                        firstLoad = false
                    } else if (_messageList.value?.last() != messages.last()) {
                        _messageList.value?.add(messages.last())
                        _newMessageReceived.postValue(true)
                    }
                }
            }
        }
    }

    fun prepareMessageForSending() {
        viewModelScope.launch {
            getUserId()?.let { uid ->
                if (!newMessageText.value.isNullOrBlank() && chat != null) {
                    val message = Message().apply {
                        id = "$uid-${System.currentTimeMillis()}"
                        userId = uid
                        text = newMessageText.value
                        timestamp = System.currentTimeMillis()
                    }

                    val chatInfo = ChatInfo().apply {
                        chatId = chat?.id
                        userId = uid
                        otherUserId = chat?.otherUserId
                    }

                    newMessageText.value = ""
                    _messageList.value?.add(message)
                    _sendMessageAttempt.postValue(true)

                    sendMessage(message, chatInfo)
                }
            }
        }
    }

    private suspend fun sendMessage(message: Message, chatInfo: ChatInfo) {
        eventsUseCase.sendMessage(chatInfo, message).collect {
            _sendMessageSuccess.postValue(it)
        }
    }

    private fun getUserId(): String? {
        return RudoChatInstance.getFirebaseAuth()?.uid
    }
}
