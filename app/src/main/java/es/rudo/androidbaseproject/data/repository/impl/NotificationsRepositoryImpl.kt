package es.rudo.androidbaseproject.data.repository.impl

import es.rudo.androidbaseproject.data.dto.Notification
import es.rudo.androidbaseproject.data.repository.NotificationsRepository
import es.rudo.androidbaseproject.data.source.remote.NotificationsDataSource
import retrofit2.Response
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val notificationsDataSource: NotificationsDataSource
) : NotificationsRepository {

    override fun sendNotification(notification: Notification): Response<Void> {
        return notificationsDataSource.sendNotification(notification)
    }
}
