package es.rudo.firebasechat.data.dto

import java.io.Serializable

data class MessageNotification(
    val topic: String,
    val notification: Notification,
    val data: DataNotification
) : Serializable
