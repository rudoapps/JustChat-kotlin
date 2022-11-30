package es.rudo.firebasechat.models

open class Group {
    var users: MutableList<UserData>? = null
    val messages: MutableList<ChatMessageItem>? = null
}
