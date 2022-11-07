package es.rudo.firebasechat.data.ws

import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.Group
import es.rudo.firebasechat.domain.models.Message

interface EventsApi {

    fun getMessage(): Message

    fun sendMessage(message: Message)

    fun getChats(): ArrayList<Chat>

    fun getGroups(): ArrayList<Group>
}
