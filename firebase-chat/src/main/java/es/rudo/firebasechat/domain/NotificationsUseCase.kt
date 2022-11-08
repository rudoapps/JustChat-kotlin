package es.rudo.firebasechat.domain

import es.rudo.firebasechat.data.dto.Notification
import retrofit2.Response

interface NotificationsUseCase {
    suspend fun sendNotification(userId: String, notification: Notification): Response<Void>
}
