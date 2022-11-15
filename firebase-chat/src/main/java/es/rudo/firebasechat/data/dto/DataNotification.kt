package es.rudo.firebasechat.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataNotification(
    val title: String,
    val message: String,

    @SerializedName("chat_id")
    val chatId: String,

    @SerializedName("chat_name")
    val chatName: String,

    @SerializedName("chat_other_user_id")
    val chatOtherUserId: String,

    @SerializedName("chat_other_user_image")
    val chatOtherUserImage: String,

    @SerializedName("chat_user_device_token")
    val userDeviceToken: String
) : Serializable
