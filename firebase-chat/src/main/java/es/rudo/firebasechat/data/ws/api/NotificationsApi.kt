package es.rudo.firebasechat.data.ws.api

import es.rudo.firebasechat.data.dto.MessageNotificationBack
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationsApi {

    @POST("fir-chat-d613e/messages:send/")
    fun sendNotification(
        @Body message: MessageNotificationBack
    ): Call<Void>
}
