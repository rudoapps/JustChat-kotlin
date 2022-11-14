package es.rudo.firebasechat.data.repository

import es.rudo.firebasechat.data.dto.Notification
import retrofit2.Response

interface NotificationsRepository {
    fun sendNotification(notification: Notification): Response<Void>
}
