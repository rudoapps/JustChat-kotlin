package es.rudo.firebasechat.utils

fun generateId(length: Int = 20): String { // ex: bwUIoWNCSQvPZh8xaFuz
    val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return alphaNumeric.shuffled().take(length).joinToString("")
}
