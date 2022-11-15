package es.rudo.firebasechat.data.source.remote.impl

import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.source.remote.NotificationsDataSource
import es.rudo.firebasechat.data.ws.api.NotificationsApi
import retrofit2.Response
import javax.inject.Inject

class NotificationsDataSourceImpl @Inject constructor(
    private val notificationsApi: NotificationsApi
) : NotificationsDataSource {

    override fun sendNotification(notification: Notification): Response<Void> {
        return notificationsApi.sendNotification(notification).execute()
    }
}
