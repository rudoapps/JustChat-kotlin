package es.rudo.justchat.models

open class Group {
    var users: MutableList<UserData>? = null
    val messages: MutableList<ChatMessageItem>? = null
}
