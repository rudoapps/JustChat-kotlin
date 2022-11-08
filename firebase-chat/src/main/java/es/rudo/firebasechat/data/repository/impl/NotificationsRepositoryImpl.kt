package es.rudo.firebasechat.data.repository.impl

import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.repository.NotificationsRepository
import es.rudo.firebasechat.data.source.remote.NotificationsDataSource
import retrofit2.Response
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val notificationsDataSource: NotificationsDataSource
) : NotificationsRepository {

    override suspend fun sendNotification(
        userId: String,
        notification: Notification
    ): Response<Void> {
        return notificationsDataSource.sendNotification(userId, notification)
    }
}
