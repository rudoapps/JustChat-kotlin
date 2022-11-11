package es.rudo.firebasechat.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataNotification(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("other_user_id")
    val otherUserId: String,

    @SerializedName("chat_id")
    val chatId: String
) : Serializable
