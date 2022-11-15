package es.rudo.firebasechat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.dto.DataNotification
import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.NotificationsUseCase
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.ChatInfo
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.main.instance.JustChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : ViewModel() {

    var chat: Chat? = null

    val newMessageText = MutableLiveData<String>()

    var message: Message? = null

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

    // TODO esto irá separado en getMessageHistory y getNewMessage
    private var firstLoad = true
    fun getMessages(isNetworkAvailable: Boolean, initialMessageList: MutableList<Message>?) {
        _messageList.value = initialMessageList ?: mutableListOf()

        viewModelScope.launch {
            chat?.let {
                eventsUseCase.getMessagesIndividual(isNetworkAvailable, it, 0).collect { messages ->
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

    fun prepareMessageForSending(isNetworkAvailable: Boolean) {
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

                    this@ChatViewModel.message = message
                    eventsUseCase.sendMessage(isNetworkAvailable, chatInfo, message).collect {
                        _sendMessageSuccess.postValue(it)
                    }
//                    sendMessage(message, chatInfo)
                }
            }
        }
    }

    private suspend fun sendMessage(
        isNetworkAvailable: Boolean,
        message: Message,
        chatInfo: ChatInfo
    ) {
        eventsUseCase.sendMessage(isNetworkAvailable, chatInfo, message).collect {
            _sendMessageSuccess.postValue(it)
        }
    }

    fun sendNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            val dataNotification = DataNotification(
                title = chat?.name.toString(),
                message = message?.text.toString(),
                chatId = chat?.id.toString()
            )

            val notification = Notification(
                to = chat?.userDeviceToken.toString(),
                data = dataNotification,
                priority = 10
            )
            val response = notificationsUseCase.sendNotification(notification)
            response
        }
    }

    private fun getUserId(): String? {
        return JustChat.getFirebaseAuth()?.uid
    }
}
