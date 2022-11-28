package es.rudo.firebasechat.helpers.preferences

import android.content.SharedPreferences

class AppPreferences(
    val sharedPreferences: SharedPreferences?
) {

    var userId: String? by preferences()

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
