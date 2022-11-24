package es.rudo.androidbaseproject.domain.models

open class Group {
    var users: MutableList<UserData>? = null
    val messages: MutableList<Message>? = null
}
