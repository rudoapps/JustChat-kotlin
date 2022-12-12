package es.rudo.androidbaseproject.helpers.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import es.rudo.androidbaseproject.ui.main.MainActivity
import es.rudo.justchat.helpers.Constants.PREFERENCES
import es.rudo.justchat.helpers.Constants.USER_ID_PREFERENCES

val Context.isNetworkAvailable: Boolean
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

fun Context.getUserId(): String? {
    return MainActivity.getFirebaseAuth()?.uid ?: run {
        getUserIdPreferences()
    }
}

fun Context.getUserIdPreferences(): String? {
    val preferences = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    return preferences.getString(USER_ID_PREFERENCES, null)
}

fun Context.saveUserId(userId: String?) {
    val preferences = this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    preferences.edit()?.putString(USER_ID_PREFERENCES, userId)?.apply()
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}
