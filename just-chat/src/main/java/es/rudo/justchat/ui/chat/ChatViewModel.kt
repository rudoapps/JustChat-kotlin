package es.rudo.justchat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.justchat.helpers.extensions.getDate
import es.rudo.justchat.helpers.extensions.getFormattedDate
import es.rudo.justchat.helpers.utils.userId
import es.rudo.justchat.main.instance.JustChat
import es.rudo.justchat.models.* // ktlint-disable no-wildcard-imports
import es.rudo.justchat.models.results.ResultInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
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

    // TODO esto irá separado en getMessageHistory y getNewMessage
    private var firstLoad = true
    fun getMessages(initialMessageList: MutableList<ChatMessageItem>?) {
        _messageList.value = getCompleteMessageList(initialMessageList)

        viewModelScope.launch {
            chat?.let { chat ->
                JustChat.events?.getChatMessages(
                    userId.toString(),
                    chat.id.toString(),
                    0
                )?.collect { messages ->
                    if (firstLoad) {
                        _messageList.postValue(getCompleteMessageList(messages))
                        firstLoad = false
                    } else if (_messageList.value?.last() != messages.last()) {
                        checkLastMessageDateAndAddMessage(messages.last())
                        _newMessageReceived.postValue(true)
                    }
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
                it
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
                        date = currentMsg.timestamp.getFormattedDate()
                    })
                }

                if (currentMsg.userId == userId && lastMsg?.userId == userId) {
                    currentMsg.position = when (lastMsg?.position) {
                        ChatMessageItem.MessagePosition.BOTTOM -> {
                            if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == userId)
                                ChatMessageItem.MessagePosition.MIDDLE
                            else
                                ChatMessageItem.MessagePosition.TOP
                        }
                        else -> { // ChatMessageItem.MessagePosition.MIDDLE
                            if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == userId)
                                ChatMessageItem.MessagePosition.MIDDLE
                            else
                                ChatMessageItem.MessagePosition.TOP
                        }
                    }
                } else {
                    if (listIterator.hasNext() && list[listIterator.nextIndex()].userId == userId)
                        ChatMessageItem.MessagePosition.BOTTOM
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
