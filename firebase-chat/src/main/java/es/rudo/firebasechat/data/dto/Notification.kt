package es.rudo.firebasechat.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Notification(
    val title: String,
    val message: String,

    @SerializedName("user_id")
    val userId: String
) : Serializable
