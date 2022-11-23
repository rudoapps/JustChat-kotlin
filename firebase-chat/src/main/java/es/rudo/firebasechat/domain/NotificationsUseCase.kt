package es.rudo.firebasechat.domain

import es.rudo.firebasechat.data.dto.Notification
import retrofit2.Response

interface NotificationsUseCase {
    fun sendNotification(notification: Notification): Response<Void>
}
