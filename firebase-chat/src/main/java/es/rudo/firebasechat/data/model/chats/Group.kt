package es.rudo.firebasechat.data.model.chats

open class Group {
    var users: MutableList<UserData>? = null
    val messages: MutableList<Message>? = null
}
