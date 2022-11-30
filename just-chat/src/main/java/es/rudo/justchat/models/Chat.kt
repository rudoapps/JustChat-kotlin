package es.rudo.justchat.models

import java.io.Serializable

class Chat : Serializable {
    var id: String? = null
    var name: String? = null
    var otherUserId: String? = null
    var otherUserImage: String? = null
    var lastMessage: String? = null
    var userDeviceToken: String? = null
    var messages: MutableList<ChatMessageItem>? = null
}
