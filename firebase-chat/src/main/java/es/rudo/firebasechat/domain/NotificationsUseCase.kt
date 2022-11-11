package es.rudo.firebasechat.domain

import es.rudo.firebasechat.data.dto.MessageNotificationBack
import retrofit2.Response

interface NotificationsUseCase {
    fun sendNotification(messageNotificationBack: MessageNotificationBack): Response<Void>
}
