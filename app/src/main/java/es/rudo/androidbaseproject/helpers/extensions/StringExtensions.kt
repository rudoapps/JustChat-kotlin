package es.rudo.androidbaseproject.helpers.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import es.rudo.androidbaseproject.helpers.Constants
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

fun String.isValidName() = matches(Constants.NAME_PATTERN.toRegex())

fun String.isValidEmail() = matches(Constants.EMAIL_PATTERN.toRegex())

fun String.isValidUsername() = matches(Constants.USERNAME_PATTERN.toRegex())

fun String.isValidPassword() = matches(Constants.PASSWORD_PATTERN.toRegex())

fun String?.fromHtml(): Spanned? {
    return when {
        this == null -> SpannableString("")
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        }
        else -> Html.fromHtml(this)
    }
}

fun String?.downloadImageFromUrl(imageInterface: ImageInterface) {
    val `in`: InputStream?
    val bitmap: Bitmap?
    val responseCode: Int
    try {
        val url = URL(this)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.doInput = true
        con.connect()
        responseCode = con.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            `in` = con.inputStream
            bitmap = BitmapFactory.decodeStream(`in`)
            `in`.close()
            imageInterface.onImageSuccess(bitmap)
        } else {
            imageInterface.onImageError(Exception(responseCode.toString()))
        }
    } catch (ex: Exception) {
        imageInterface.onImageError(ex)
    }
}

interface ImageInterface {
    fun onImageSuccess(bitmap: Bitmap)
    fun onImageError(exception: Exception)
}
