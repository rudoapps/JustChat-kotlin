package es.rudo.firebasechat.data.dto

import java.io.Serializable

data class DataNotification(
    val title: String,
    val message: String,
    val chatId: String
) : Serializable
