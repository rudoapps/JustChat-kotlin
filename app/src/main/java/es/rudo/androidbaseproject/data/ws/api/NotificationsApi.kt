package es.rudo.androidbaseproject.data.ws.api

import es.rudo.androidbaseproject.data.dto.Notification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationsApi {

    @POST("fcm/send")
    fun sendNotification(@Body message: Notification): Call<Void>
}
