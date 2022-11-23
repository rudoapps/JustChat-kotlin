package es.rudo.firebasechat.data.dto

import java.io.Serializable

data class Notification(
    var to: String,
    val data: DataNotification,
    val priority: Int
) : Serializable
