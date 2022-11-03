package es.rudo.firebasechat.data.ws

import es.rudo.firebasechat.data.model.chats.Chat
import es.rudo.firebasechat.data.model.chats.Group
import es.rudo.firebasechat.data.model.chats.Message

interface EventsApi {

    fun getMessage(): Message

    fun sendMessage(message: Message)

    fun getChats(): ArrayList<Chat>

    fun getGroups(): ArrayList<Group>
}
