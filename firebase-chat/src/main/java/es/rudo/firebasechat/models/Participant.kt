package es.rudo.firebasechat.models

import java.io.Serializable

class Participant : Serializable {
    var userId: String? = null
    var userName: String? = null
    var userImage: String? = null
}