package es.rudo.justchat.helpers.preferences

import android.content.SharedPreferences

class AppPreferences(
    val sharedPreferences: SharedPreferences?
) {

    var userId: String? by preferences()

    var chatId: String? by preferences()

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
