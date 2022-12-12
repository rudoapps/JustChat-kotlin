package es.rudo.androidbaseproject.domain.impl

import es.rudo.androidbaseproject.data.dto.Notification
import es.rudo.androidbaseproject.data.repository.NotificationsRepository
import es.rudo.androidbaseproject.domain.NotificationsUseCase
import retrofit2.Response
import javax.inject.Inject

class NotificationsUseCaseImpl @Inject constructor(private val notificationsRepository: NotificationsRepository) :
    NotificationsUseCase {

    override fun sendNotification(notification: Notification): Response<Void> {
        return notificationsRepository.sendNotification(notification)
    }
}
