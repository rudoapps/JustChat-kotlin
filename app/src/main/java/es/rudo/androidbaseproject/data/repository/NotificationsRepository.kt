package es.rudo.androidbaseproject.data.repository

import es.rudo.androidbaseproject.data.dto.Notification
import retrofit2.Response

interface NotificationsRepository {
    fun sendNotification(notification: Notification): Response<Void>
}
