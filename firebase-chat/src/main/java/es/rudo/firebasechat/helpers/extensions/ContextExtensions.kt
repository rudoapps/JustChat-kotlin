package es.rudo.firebasechat.helpers.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import es.rudo.firebasechat.helpers.Constants
import es.rudo.firebasechat.main.instance.JustChat

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

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.getUserId(): String? {
    return JustChat.getFirebaseAuth()?.uid ?: run {
        getUserIdPreferences()
    }
}

fun Context.getUserIdPreferences(): String? {
    val preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    return preferences.getString(Constants.USER_ID_PREFERENCES, null)
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}
