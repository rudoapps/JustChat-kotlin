package es.rudo.justchat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.justchat.helpers.extensions.getDate
import es.rudo.justchat.helpers.extensions.getFormattedDate
import es.rudo.justchat.main.instance.JustChat
import es.rudo.justchat.models.*
import es.rudo.justchat.models.results.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val TIME_SPAN_MILLIS = 300000 // 5 minutes

    var chat: Chat? = null
    var userId: String? = null

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

    fun getMessageHistory(initialMessageList: MutableList<ChatMessageItem>?) {
        _messageList.value = getCompleteMessageList(initialMessageList)

        viewModelScope.launch {
            chat?.let { chat ->
                JustChat.events?.getChatMessages(
                    userId.toString(),
                    chat.id.toString(),
                    0
                )?.collect { messages ->
                    _messageList.postValue(getCompleteMessageList(messages))
                }
            }
        }
    }

    fun initFlowReceiveMessage() {
        viewModelScope.launch {
            JustChat.events?.initFlowReceiveMessage(
                userId.toString(),
                chat?.id.toString()
            )?.collect {
                checkLastMessageDateAndAddMessage(it)
                _newMessageReceived.postValue(true)
            }
        }
    }

    // TODO repensar para incluir paginación
    private fun getCompleteMessageList(messageList: MutableList<ChatMessageItem>?): MutableList<ChatBaseItem> {
        val finalList = mutableListOf<ChatBaseItem>()

        messageList?.let { list ->
            val listIterator = list.listIterator()
            var lastMsg: ChatMessageItem? = null
            var lastDateMsg: ChatMessageItem? = null

            while (listIterator.hasNext()) {
                val currentMsg = listIterator.next()

                if (currentMsg.timestamp.getDate() != lastDateMsg?.timestamp.getDate()) {
                    lastDateMsg = currentMsg
                    finalList.add(ChatDateItem().apply {
                        id = currentMsg.timestamp.getDate()
                        originalTimestamp = currentMsg.timestamp
                        date = currentMsg.timestamp.getFormattedDate()
                    })
                }

                currentMsg.position = if (currentMsg.userId == lastMsg?.userId) {
                    if (listIterator.hasNext() &&
                            (list[listIterator.nextIndex()].timestamp.getDate() != currentMsg.timestamp.getDate() ||
                            (list[listIterator.nextIndex()].timestamp?.minus(currentMsg.timestamp ?: 0) ?: 0) > TIME_SPAN_MILLIS)) {
                        if (lastMsg?.position == ChatMessageItem.MessagePosition.SINGLE)
                            ChatMessageItem.MessagePosition.SINGLE
                        else
                            ChatMessageItem.MessagePosition.BOTTOM
                    } else {
                        when (lastMsg?.position) {
                            ChatMessageItem.MessagePosition.TOP -> {
                                if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == currentMsg.userId)
                                    ChatMessageItem.MessagePosition.MIDDLE
                                else
                                    ChatMessageItem.MessagePosition.BOTTOM
                            }
                            ChatMessageItem.MessagePosition.MIDDLE -> {
                                if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == currentMsg.userId)
                                    ChatMessageItem.MessagePosition.MIDDLE
                                else
                                    ChatMessageItem.MessagePosition.BOTTOM
                            }
                            else -> { // ChatMessageItem.MessagePosition.SINGLE
                                if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == currentMsg.userId)
                                    ChatMessageItem.MessagePosition.TOP
                                else
                                    ChatMessageItem.MessagePosition.SINGLE
                            }
                        }
                    }
                } else {
                    if (listIterator.hasNext() &&
                            (list[listIterator.nextIndex()].timestamp.getDate() != currentMsg.timestamp.getDate() ||
                            (list[listIterator.nextIndex()].timestamp?.minus(currentMsg.timestamp ?: 0) ?: 0) > TIME_SPAN_MILLIS))
                        ChatMessageItem.MessagePosition.SINGLE
                    else if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == currentMsg.userId)
                        ChatMessageItem.MessagePosition.TOP
                    else
                        ChatMessageItem.MessagePosition.SINGLE
                }

                lastMsg = currentMsg

                finalList.add(currentMsg)
            }
        }

        return finalList
    }

    //TODO valorar lanzar una actualización de fechas si se añade una nueva (puede haber pasado de día durante la convo)
    private fun checkLastMessageDateAndAddMessage(message: ChatMessageItem) {
        (_messageList.value?.lastOrNull() as? ChatMessageItem)?.let { lastMessage ->
            if (lastMessage.timestamp.getDate() != message.timestamp.getDate()) {
                _messageList.value?.add(
                    ChatDateItem().apply {
                        id = message.timestamp.getDate()
                        originalTimestamp = message.timestamp
                        date = message.timestamp.getFormattedDate()
                    }
                )

                _messageList.value?.add(message.apply {
                    position = ChatMessageItem.MessagePosition.SINGLE
                })
            } else {
                if ((message.timestamp?.minus(lastMessage.timestamp ?: 0) ?: 0) > TIME_SPAN_MILLIS) {
                    _messageList.value?.add(message.apply {
                        position = ChatMessageItem.MessagePosition.SINGLE
                    })
                } else {
                    when (lastMessage.position) {
                        ChatMessageItem.MessagePosition.SINGLE -> {
                            lastMessage.position = ChatMessageItem.MessagePosition.TOP
                            _messageList.value?.add(message.apply {
                                position = ChatMessageItem.MessagePosition.BOTTOM
                            })
                        }
                        else -> { // ChatMessageItem.MessagePosition.BOTTOM
                            lastMessage.position = ChatMessageItem.MessagePosition.MIDDLE
                            _messageList.value?.add(message.apply {
                                position = ChatMessageItem.MessagePosition.BOTTOM
                            })
                        }
                    }
                }
            }
        } ?: run {
            _messageList.value?.add(
                ChatDateItem().apply {
                    id = message.timestamp.getDate()
                    originalTimestamp = message.timestamp
                    date = message.timestamp.getFormattedDate()
                }
            )

            _messageList.value?.add(message.apply {
                position = ChatMessageItem.MessagePosition.SINGLE
            })
        }
    }

    fun prepareMessageForSending(idUser: String?) {
        viewModelScope.launch {
            userId?.let { uid ->
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
