package es.rudo.firebasechat.data.ws.api

import es.rudo.firebasechat.data.dto.Notification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationsApi {

    @POST("wp/{server_key}/")
    fun sendNotification(
        @Path("server_key") serverKey: String,
        @Body message: Notification
    ): Call<Void>
}
