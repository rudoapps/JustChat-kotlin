package es.rudo.firebasechat.data.source.remote

import es.rudo.firebasechat.data.dto.MessageNotificationBack
import retrofit2.Response

interface NotificationsDataSource {
    fun sendNotification(messageNotificationBack: MessageNotificationBack): Response<Void>
}
