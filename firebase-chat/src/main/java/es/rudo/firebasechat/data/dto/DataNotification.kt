package es.rudo.firebasechat.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataNotification(
    @SerializedName("chat_id")
    val chatId: String,

    @SerializedName("chat_destination_user_name")
    val chatDestinationUserName: String,

    @SerializedName("chat_destination_user_id")
    val chatDestinationUserId: String,

    @SerializedName("chat_destination_user_image")
    val chatDestinationUserImage: String,

    @SerializedName("destination_user_device_token")
    val destinationUserDeviceToken: String,

    @SerializedName("chat_message")
    val chatMessage: String
) : Serializable
