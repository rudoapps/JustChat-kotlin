package es.rudo.firebasechat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rudo.firebasechat.data.dto.DataNotification
import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.source.preferences.AppPreferences
import es.rudo.firebasechat.domain.EventsUseCase
import es.rudo.firebasechat.domain.NotificationsUseCase
import es.rudo.firebasechat.domain.models.*
import es.rudo.firebasechat.helpers.extensions.getDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase,
    private val notificationsUseCase: NotificationsUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    var chat: Chat? = null

    val newMessageText = MutableLiveData<String>()

    var lastMessageSent: ChatMessageItem? = null

    private val _messageList = MutableLiveData<MutableList<ChatBaseItem>>()
    val messageList: LiveData<MutableList<ChatBaseItem>> = _messageList

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
    fun getMessages(isNetworkAvailable: Boolean, initialMessageList: MutableList<ChatMessageItem>?) {
        _messageList.value = addDateChatItems(initialMessageList)

        viewModelScope.launch {
            chat?.let {
                eventsUseCase.getMessagesIndividual(isNetworkAvailable, it, 0).collect { messages ->
                    if (firstLoad) {
                        _messageList.postValue(addDateChatItems(messages))
                        firstLoad = false
                    } else if (_messageList.value?.last() != messages.last()) {
                        _messageList.value?.add(messages.last())
                        _newMessageReceived.postValue(true)
                    }
                }
            }
        }
    }

    //TODO repensar para incluir paginación
    private fun addDateChatItems(messageList: MutableList<ChatMessageItem>?): MutableList<ChatBaseItem> {
        val finalList = mutableListOf<ChatBaseItem>()

        messageList?.let { list ->
            var lastDate = list.first().timestamp.getDate()
            finalList.add(ChatDateItem().apply {
                id = lastDate
                date = lastDate
            })

            for (message in list) {
                if (message.timestamp.getDate() != lastDate) {
                    lastDate = message.timestamp.getDate()
                    finalList.add(ChatDateItem().apply {
                        id = lastDate
                        date = lastDate
                    })
                }

                finalList.add(message)
            }
        }

        return finalList
    }

    private fun checkLastMessageDate() {
        _messageList.value?.last()
    }

    fun prepareMessageForSending(idUser: String?, isNetworkAvailable: Boolean) {
        viewModelScope.launch {
            idUser?.let { uid ->
                if (!newMessageText.value.isNullOrBlank() && chat != null) {
                    val message = ChatMessageItem().apply {
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

                    this@ChatViewModel.lastMessageSent = message
                    
                    sendMessage(isNetworkAvailable, message, chatInfo)
                }
            }
        }
    }

    private suspend fun sendMessage(
        isNetworkAvailable: Boolean,
        message: ChatMessageItem,
        chatInfo: ChatInfo
    ) {
        eventsUseCase.sendMessage(isNetworkAvailable, chatInfo, message).collect {
            _sendMessageSuccess.postValue(it)
        }
    }

    fun sendNotification(isNetworkAvailable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsUseCase.getCurrentUser(isNetworkAvailable).collect {
                val dataNotification = DataNotification(
                    chatId = chat?.id.toString(),
                    chatDestinationUserName = it.userName.toString(),
                    chatDestinationUserId = it.userId.toString(),
                    chatDestinationUserImage = it.userPhoto.toString(),
                    destinationUserDeviceToken = it.userDeviceToken.toString(),
                    chatMessage = lastMessageSent?.text.toString()
                )
//            "e1ZrKOmgTc6AFtgJYxiXVU:APA91bFYH2pZz9M3DrycsO7ko2awfMICnrxN2BRviS-0oBh01OqBXZDz3qZC-v4LOwQQrK6tV3Vcw7GmYAeoi5AX7zNJ5ugHF1K29MeXvOFVF9duBD-wmG8nTygVejjXzSZ7Fbdf7oim",
// chat?.userDeviceToken.toString(),
                val notification = Notification(
                    to = chat?.userDeviceToken.toString(),
                    data = dataNotification,
                    priority = 10
                )
                val response = notificationsUseCase.sendNotification(notification)
            }
        }
    }

    fun manageChatId(save: Boolean) {
        if (save) {
            appPreferences.chatId = chat?.id.toString()
        } else {
            appPreferences.chatId = ""
        }
    }
}
