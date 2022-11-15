package es.rudo.firebasechat.data.source.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    val sharedPreferences: SharedPreferences?
) {

    var accessToken: String? by preferences()

    var refreshToken: String? by preferences()

    var chatId: String? by preferences()

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
