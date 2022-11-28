package es.rudo.firebasechat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.firebasechat.helpers.extensions.getDate
import es.rudo.firebasechat.helpers.extensions.getFormattedDate
import es.rudo.firebasechat.main.instance.JustChat
import es.rudo.firebasechat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.firebasechat.models.results.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val userId: String?
        get() =
            JustChat.userId ?: run {
                JustChat.appPreferences?.userId
            }

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
    fun getMessages(initialMessageList: MutableList<ChatMessageItem>?) {
        _messageList.value = addDateChatItems(initialMessageList)

        viewModelScope.launch {
            chat?.let { chat ->
//                initFlowReceiveMessage(isNetworkAvailable, userId.toString(), chat.id.toString())
                JustChat.events?.getChatMessages(
                    userId.toString(),
                    chat.id.toString(),
                    0
                )?.collect { messages ->
                    if (firstLoad) {
                        _messageList.postValue(addDateChatItems(messages))
                        firstLoad = false
                    } else if (_messageList.value?.last() != messages.last()) {
                        checkLastMessageDateAndAddMessage(messages.last())
                        _newMessageReceived.postValue(true)
                    }
                }
            }
        }
    }

    fun initFlowReceiveMessage(userId: String, chatId: String) {
        viewModelScope.launch {
            JustChat.events?.initFlowReceiveMessage(
                userId,
                chatId
            )?.collect {
                it
            }
        }
    }

    // TODO repensar para incluir paginación
    private fun addDateChatItems(messageList: MutableList<ChatMessageItem>?): MutableList<ChatBaseItem> {
        val finalList = mutableListOf<ChatBaseItem>()

        messageList?.let { list ->
            var lastDate = list.firstOrNull()?.timestamp
            finalList.add(
                ChatDateItem().apply {
                    id = lastDate.getDate()
                    date = lastDate.getFormattedDate()
                }
            )

            for (message in list) {
                if (message.timestamp.getDate() != lastDate.getDate()) {
                    lastDate = message.timestamp
                    finalList.add(
                        ChatDateItem().apply {
                            id = lastDate.getDate()
                            date = lastDate.getFormattedDate()
                        }
                    )
                }

                finalList.add(message)
            }
        }

        return finalList
    }

    private fun checkLastMessageDateAndAddMessage(message: ChatMessageItem) {
        (_messageList.value?.lastOrNull() as? ChatMessageItem)?.let {
            if (it.timestamp.getDate() != message.timestamp.getDate()) {
                _messageList.value?.add(
                    ChatDateItem().apply {
                        id = message.timestamp.getDate()
                        date = message.timestamp.getFormattedDate()
                    }
                )
            }
        } ?: run {
            _messageList.value?.add(
                ChatDateItem().apply {
                    id = message.timestamp.getDate()
                    date = message.timestamp.getFormattedDate()
                }
            )
        }

        _messageList.value?.add(message)
    }

    fun prepareMessageForSending(idUser: String?) {
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
                    checkLastMessageDateAndAddMessage(message)
                    _sendMessageAttempt.postValue(true)

                    this@ChatViewModel.lastMessageSent = message

                    sendMessage(message, chatInfo)
                }
            }
        }
    }

    private suspend fun sendMessage(message: ChatMessageItem, chatInfo: ChatInfo) {
        JustChat.events?.sendMessage(chatInfo, message)?.collect {
            _sendMessageSuccess.postValue(it)
        }
    }

    fun sendNotification() {
        viewModelScope.launch(Dispatchers.IO) {
            JustChat.events?.sendNotification(
                userId.toString(),
                chat,
                lastMessageSent?.text
            )
        }
    }
}
