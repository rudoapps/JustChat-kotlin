package es.rudo.firebasechat.data.ws

import es.rudo.firebasechat.domain.models.*

interface EventsApi {

    fun getMessage(): Message

    fun sendMessage(message: Message)

    fun getChats(): ArrayList<Chat>

    fun getGroups(): ArrayList<Group>
}
