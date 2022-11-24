package es.rudo.androidbaseproject.helpers.extensions

import es.rudo.firebasechat.helpers.Constants
import java.text.SimpleDateFormat
import java.util.*

fun Long?.getTime(): String {
    return this.parseDate(Constants.SIMPLE_DATE_FORMAT_TIME) ?: ""
}

fun Long?.getDate(): String {
    return this.parseDate(Constants.SIMPLE_DATE_FORMAT_DATE) ?: ""
}

fun Long?.getAllDate(): String {
    return this.parseDate(Constants.SIMPLE_DATE_FORMAT_COMPLETE) ?: ""
}

fun Long?.parseDate(format: String): String? {
    return this?.let {
        val date = Date(it)
        val result = SimpleDateFormat(format, Locale.UK)
        result.format(date)
    } ?: kotlin.run {
        null
    }
}
