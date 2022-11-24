package es.rudo.firebasechat.domain.models

open class Group {
    var users: MutableList<UserData>? = null
    val messages: MutableList<ChatMessageItem>? = null
}
