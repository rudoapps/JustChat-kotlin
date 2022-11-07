package es.rudo.firebasechat.utils

import es.rudo.firebasechat.data.dto.results.ResultInfo
import es.rudo.firebasechat.data.dto.results.ResultUserChat

fun generateId(length: Int = 20): String { // ex: bwUIoWNCSQvPZh8xaFuz
    val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return alphaNumeric.shuffled().take(length).joinToString("")
}

fun MutableList<Pair<String, String>>.getPair(id: String): Pair<String, String>? {
    for (pair in this) {
        if (pair.first == id) {
            return pair
        }
    }
    return null
}

fun getResult(isSuccess: Boolean, exception: Exception? = null): ResultInfo {
    return ResultInfo().apply {
        success = isSuccess
        error = exception
    }
}

fun getResultUserChat(
    exist: Boolean? = null,
    isSuccess: Boolean,
    exception: Exception? = null
): ResultUserChat {
    return ResultUserChat().apply {
        exists = exist
        success = isSuccess
        error = exception
    }
}
