package es.rudo.firebasechat.data.source.remote.impl

import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.source.remote.NotificationsDataSource
import es.rudo.firebasechat.data.ws.NotificationsApi
import es.rudo.firebasechat.utils.*
import retrofit2.Response
import javax.inject.Inject

class NotificationsDataSourceImpl @Inject constructor(
    private val notificationsApi: NotificationsApi
) : NotificationsDataSource, CoroutineAppRepository() {

    override suspend fun sendNotification(
        userId: String,
        notification: Notification
    ): Response<Void> {
        return coroutineBackground {
            val response = notificationsApi.sendNotification(userId, notification).execute()
            if (response.isSuccessful) {
                response
            } else {
                throw Exception()
            }
        }
    }
}
