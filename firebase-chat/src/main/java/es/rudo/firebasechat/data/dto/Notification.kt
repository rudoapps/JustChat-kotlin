package es.rudo.firebasechat.data.dto

import java.io.Serializable

data class Notification(
    val title: String,
    val body: String
) : Serializable
