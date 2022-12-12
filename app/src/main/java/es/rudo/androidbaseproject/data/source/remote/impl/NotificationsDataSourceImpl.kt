package es.rudo.androidbaseproject.data.source.remote.impl

import es.rudo.androidbaseproject.data.dto.Notification
import es.rudo.androidbaseproject.data.source.remote.NotificationsDataSource
import es.rudo.androidbaseproject.data.ws.api.NotificationsApi
import retrofit2.Response
import javax.inject.Inject

class NotificationsDataSourceImpl @Inject constructor(
    private val notificationsApi: NotificationsApi
) : NotificationsDataSource {

    override fun sendNotification(notification: Notification): Response<Void> {
        return notificationsApi.sendNotification(notification).execute()
    }
}
