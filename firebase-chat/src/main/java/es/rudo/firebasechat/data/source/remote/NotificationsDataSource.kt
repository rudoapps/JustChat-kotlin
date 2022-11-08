package es.rudo.firebasechat.data.source.remote

import es.rudo.firebasechat.data.dto.Notification
import retrofit2.Response

interface NotificationsDataSource {
    fun sendNotification(userId: String, notification: Notification): Response<Void>
}
