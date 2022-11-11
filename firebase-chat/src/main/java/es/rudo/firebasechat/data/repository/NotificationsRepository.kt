package es.rudo.firebasechat.data.repository

import es.rudo.firebasechat.data.dto.MessageNotificationBack
import retrofit2.Response

interface NotificationsRepository {
    fun sendNotification(messageNotificationBack: MessageNotificationBack): Response<Void>
}
