package es.rudo.androidbaseproject.data.dto

import es.rudo.androidbaseproject.data.dto.DataNotification
import java.io.Serializable

data class Notification(
    var to: String,
    val data: DataNotification,
    val priority: Int
) : Serializable
