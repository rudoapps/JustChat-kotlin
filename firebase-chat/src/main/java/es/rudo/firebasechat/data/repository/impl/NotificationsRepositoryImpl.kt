package es.rudo.firebasechat.data.repository.impl

import es.rudo.firebasechat.data.dto.MessageNotificationBack
import es.rudo.firebasechat.data.repository.NotificationsRepository
import es.rudo.firebasechat.data.source.remote.NotificationsDataSource
import retrofit2.Response
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val notificationsDataSource: NotificationsDataSource
) : NotificationsRepository {

    override fun sendNotification(messageNotificationBack: MessageNotificationBack): Response<Void> {
        return notificationsDataSource.sendNotification(messageNotificationBack)
    }
}
