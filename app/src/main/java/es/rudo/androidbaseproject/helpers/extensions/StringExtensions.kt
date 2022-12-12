package es.rudo.androidbaseproject.helpers.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
