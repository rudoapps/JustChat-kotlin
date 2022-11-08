package es.rudo.firebasechat.domain.impl

import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.repository.NotificationsRepository
import es.rudo.firebasechat.domain.NotificationsUseCase
import retrofit2.Response
import javax.inject.Inject

class NotificationsUseCaseImpl @Inject constructor(private val notificationsRepository: NotificationsRepository) :
    NotificationsUseCase {

    override suspend fun sendNotification(
        userId: String,
        notification: Notification
    ): Response<Void> {
        return notificationsRepository.sendNotification(userId, notification)
    }
}
