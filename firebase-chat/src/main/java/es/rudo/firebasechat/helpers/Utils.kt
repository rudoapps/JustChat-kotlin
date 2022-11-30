package es.rudo.firebasechat.helpers

import es.rudo.firebasechat.main.instance.JustChat

val userId: String?
    get() =
        JustChat.userId ?: run {
            JustChat.appPreferences?.userId
        }
