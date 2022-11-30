package es.rudo.justchat.helpers.extensions

import android.text.format.DateUtils
import es.rudo.justchat.helpers.Constants.SIMPLE_DATE_FORMAT_COMPLETE
import es.rudo.justchat.helpers.Constants.SIMPLE_DATE_FORMAT_DATE
import es.rudo.justchat.helpers.Constants.SIMPLE_DATE_FORMAT_TIME
import java.text.SimpleDateFormat
import java.util.*

fun Long?.getTime(): String {
    return this.parseDate(SIMPLE_DATE_FORMAT_TIME) ?: ""
}

fun Long?.getDate(): String {
    return this.parseDate(SIMPLE_DATE_FORMAT_DATE) ?: ""
}

fun Long?.getAllDate(): String {
    return this.parseDate(SIMPLE_DATE_FORMAT_COMPLETE) ?: ""
}

fun Long?.parseDate(format: String): String? {
    return this?.let {
        val date = Date(it)
        val result = SimpleDateFormat(format, Locale.getDefault())
        result.format(date)
    } ?: kotlin.run {
        null
    }
}

fun Long?.getFormattedDate(): String {
    return this?.let {
        DateUtils.getRelativeTimeSpanString(
            it,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS,
            DateUtils.FORMAT_SHOW_YEAR
        ).toString()
    } ?: ""
}
