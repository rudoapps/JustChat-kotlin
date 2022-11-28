package es.rudo.androidbaseproject.helpers

object Constants {
    // REGEXP PATTERNS
    const val USERNAME_PATTERN = "^[\\w.@+-]+\\Z"
    const val NAME_PATTERN = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\h]+$"
    const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z._-]+\\.+[a-z]+"
    const val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[A-ZÑÁÉÍÓÚ]).{8,})"

    // DATE AND TIME PATTERNS
    const val SHORT_DATE_PATTERN = "dd MMM"
    const val MEDIUM_DATE_PATTERN = "dd/MM/yyyy"
    const val SERVER_DATE_PATTERN = "yyyy-MM-dd"
    const val SERVER_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    const val SERVER_USER_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    const val IMAGE_DATETIME_PATTERN = "yyyyMMdd_HHmmss"

    const val HOUR_PATTERN = "HH:mm"
    const val LONG_HOUR_PATTERN = "HH:mm:ss"
    const val LONG_DATETIME_PATTERN = "dd/MM/yyyy • hh:mm aa"

    // WEBSERVICES CODES
    const val SERVER_SUCCESS_CODE = 200
    const val SERVER_CREATED_CODE = 201
    const val SERVER_NOCONTENT_CODE = 204
    const val SERVER_BADREQUEST_CODE = 400
    const val SERVER_UNAUTHORIZED_CODE = 401
    const val SERVER_FORBIDDEN_CODE = 403
    const val SERVER_NOTFOUND_CODE = 404
    const val SERVER_TIMEOUT_CODE = 408
    const val SERVER_CONFLICT_CODE = 409
    const val SERVER_INTERNALSERVER_CODE = 500
    const val SERVER_BANNED_CODE = 403

    // DIALOG CODES
    const val DIALOG_KEY_TITLE = "title"
    const val DIALOG_KEY_DESCRIPTION = "description"
    const val DIALOG_KEY_ACCEPT = "accept"
    const val DIALOG_KEY_CANCEL = "cancel"
    const val DIALOG_KEY_NON_CANCELABLE = "non_cancelable"

    // Sign in
    const val CLIENT_ID = "clientId"
    const val REQ_ONE_TAP = 2

    // Firebase database
    const val DATABASE_REFERENCE = "databaseReference"
    const val DEFAULT_NODE_FIREBASE = "users"
    const val LIMIT_MESSAGES = 10
    const val LIMIT_SIZE_ID = 30

    // Bundle extras
    const val LIST_CHATS = "listChats"
    const val EXTRAS = "extras"
    const val CHAT = "chat"

    // Date
    const val SIMPLE_DATE_FORMAT_DATE = "dd/MM/yyyy"
    const val SIMPLE_DATE_FORMAT_TIME = "HH:mm"
    const val SIMPLE_DATE_FORMAT_COMPLETE = "dd/MM/yyyy HH:mm:ss"

    // User info
    const val DEFAULT_USER_PHOTO =
        "https://i.pinimg.com/736x/94/8b/63/948b639a4022a766d5f9b62e6296f3f7.jpg"
}
