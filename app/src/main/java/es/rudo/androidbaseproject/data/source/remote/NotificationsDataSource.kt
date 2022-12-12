package es.rudo.androidbaseproject.data.source.remote

import es.rudo.androidbaseproject.data.dto.Notification
import retrofit2.Response

interface NotificationsDataSource {
    fun sendNotification(notification: Notification): Response<Void>
}
