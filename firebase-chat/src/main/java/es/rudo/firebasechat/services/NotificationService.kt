package es.rudo.firebasechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import es.rudo.firebasechat.R
import es.rudo.firebasechat.domain.models.Chat
import es.rudo.firebasechat.domain.models.Message
import es.rudo.firebasechat.helpers.Constants
import es.rudo.firebasechat.main.instance.JustChat
import es.rudo.firebasechat.ui.chat.ChatActivity
import es.rudo.firebasechat.ui.chat_list.ChatListActivity
import kotlin.random.Random

class NotificationService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "message_channel"
    }

    override fun onNewToken(token: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            FirebaseMessaging.getInstance().subscribeToTopic(it.result)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val preferences =
            applicationContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val chatId = message.data["chat_id"]
        if (preferences.getString("KEY_CHATID", "") == chatId) {
            return
        } else {
            val chat = Chat().apply {
                id = chatId
                name = message.data["chat_name"]
                otherUserId = message.data["chat_other_user_id"]
                otherUserImage = message.data["chat_other_user_image"]
                userDeviceToken = message.data["chat_user_device_token"]
            }

            val intent = if (JustChat.getFirebaseAuth() == null) {
                Intent(applicationContext, ChatListActivity::class.java)
            } else {
                Intent(applicationContext, ChatActivity::class.java).apply {
                    putExtra(Constants.CHAT, chat)
                }
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = Random.nextInt(3000)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels(notificationManager)
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.notification_ichika)

            val notificationSoundUri: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_ichika)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(message.data["title"])
                    .setContentText(message.data["message"])
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent)

            notificationBuilder.color =
                ContextCompat.getColor(applicationContext, R.color.purple_200)
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    override fun onMessageSent(msgId: String) {
        Log.d("_TAG_", "msgId: $msgId")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val messageChannelName: CharSequence = "New notification"
        val messageChannelDescription = "Device to device notification"
        val messageChannel = NotificationChannel(
            CHANNEL_ID,
            messageChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        messageChannel.description = messageChannelDescription
        messageChannel.enableLights(true)
        messageChannel.lightColor = Color.RED
        messageChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(messageChannel)
    }
}
