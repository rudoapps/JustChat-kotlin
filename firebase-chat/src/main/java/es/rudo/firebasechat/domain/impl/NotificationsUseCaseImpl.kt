package es.rudo.firebasechat.domain.impl

import es.rudo.firebasechat.data.dto.MessageNotificationBack
import es.rudo.firebasechat.data.dto.Notification
import es.rudo.firebasechat.data.repository.NotificationsRepository
import es.rudo.firebasechat.domain.NotificationsUseCase
import retrofit2.Response
import javax.inject.Inject

class NotificationsUseCaseImpl @Inject constructor(private val notificationsRepository: NotificationsRepository) :
    NotificationsUseCase {

    override fun sendNotification(messageNotificationBack: MessageNotificationBack): Response<Void> {
        return notificationsRepository.sendNotification(messageNotificationBack)
    }
}
