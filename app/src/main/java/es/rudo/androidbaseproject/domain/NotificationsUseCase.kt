package es.rudo.androidbaseproject.domain

import es.rudo.androidbaseproject.data.dto.Notification
import retrofit2.Response

interface NotificationsUseCase {
    fun sendNotification(notification: Notification): Response<Void>
}
