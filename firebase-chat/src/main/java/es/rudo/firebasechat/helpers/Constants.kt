package es.rudo.firebasechat.helpers

object Constants {

    // Sign in
    const val CLIENT_ID = "clientId"
    const val REQ_ONE_TAP = 2

    // Firebase database
    const val DATABASE_REFERENCE = "databaseReference"
    const val DEFAULT_NODE_FIREBASE = "users"
    const val LIMIT_MESSAGES = 20
    const val LIMIT_SIZE_ID = 30

    // Bundle extras
    const val LIST_CHATS = "listChats"
    const val EXTRAS = "extras"
    const val CHAT = "chat"

    // Preferences key
    const val PREFERENCES = "MyPreferences"
    const val CHAT_ID_PREFERENCES = "KEY_CHATID"
    const val USER_ID_PREFERENCES = "KEY_USERID"

    // Date
    const val SIMPLE_DATE_FORMAT_DATE = "dd/MM/yyyy"
    const val SIMPLE_DATE_FORMAT_TIME = "HH:mm"
    const val SIMPLE_DATE_FORMAT_COMPLETE = "dd/MM/yyyy HH:mm:ss"

    // User info
    const val DEFAULT_USER_PHOTO =
        "https://i.pinimg.com/736x/94/8b/63/948b639a4022a766d5f9b62e6296f3f7.jpg"
}
