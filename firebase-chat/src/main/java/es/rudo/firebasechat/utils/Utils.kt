package es.rudo.firebasechat.utils

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
