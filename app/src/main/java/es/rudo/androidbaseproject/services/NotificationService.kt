package es.rudo.androidbaseproject.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import es.rudo.androidbaseproject.helpers.Constants
import es.rudo.androidbaseproject.helpers.extensions.ImageInterface
import es.rudo.androidbaseproject.helpers.extensions.downloadImageFromUrl
import es.rudo.androidbaseproject.ui.main.MainActivity
import es.rudo.firebasechat.R
import es.rudo.firebasechat.helpers.Constants.CHAT_ID_PREFERENCES
import es.rudo.firebasechat.helpers.Constants.PREFERENCES
import es.rudo.firebasechat.models.Chat
import es.rudo.firebasechat.ui.chat.ChatActivity
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
            applicationContext.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        val chatId = message.data["chat_id"]
        val chatIdPreferences = preferences.getString(CHAT_ID_PREFERENCES, "")
        val normalizedChatIdPreferences = chatIdPreferences?.removePrefix("\"")?.removeSuffix("\"")
        if (normalizedChatIdPreferences == chatId) { // If user is in the same chat, avoid sending notification
            return
        } else {
            val chatDestinationUserImage = message.data["chat_destination_user_image"]
            chatDestinationUserImage.downloadImageFromUrl(object : ImageInterface {
                override fun onImageSuccess(bitmap: Bitmap) {
                    createNotification(message, bitmap)
                }

                override fun onImageError(exception: Exception) {
                    createNotification(message)
                }
            })
        }
    }

    private fun createNotification(message: RemoteMessage, bitmap: Bitmap? = null) {
        val chatId = message.data["chat_id"]
        val chatMessage = message.data["chat_message"]
        val chatDestinationUserName = message.data["chat_destination_user_name"]
        val chatDestinationUserId = message.data["chat_destination_user_id"]
        val chatDestinationUserImage = message.data["chat_destination_user_image"]
        val chatDestinationUserDeviceToken = message.data["destination_user_device_token"]

        val chat = Chat().apply {
            id = chatId
            name = chatDestinationUserName
            otherUserId = chatDestinationUserId
            otherUserImage = chatDestinationUserImage
            userDeviceToken = chatDestinationUserDeviceToken
        }

        val intent = if (MainActivity.getFirebaseAuth() == null) {
            Intent(applicationContext, MainActivity::class.java)
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

        val largeIcon = bitmap?.let {
            it
        } ?: kotlin.run {
            BitmapFactory.decodeResource(resources, R.drawable.notification_ichika)
        }

        val notificationSoundUri: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setLargeIcon(largeIcon)
                .setContentTitle(chatDestinationUserName)
                .setContentText(chatMessage)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

        notificationBuilder.color =
            ContextCompat.getColor(applicationContext, R.color.purple_200)
        notificationManager.notify(notificationId, notificationBuilder.build())
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
