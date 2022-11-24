package es.rudo.androidbaseproject.data.source.preferences

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

    var userId: String? by preferences()

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
