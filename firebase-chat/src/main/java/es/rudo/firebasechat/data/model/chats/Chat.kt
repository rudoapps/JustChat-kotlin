package es.rudo.firebasechat.data.model.chats

class Chat {
    var id: String? = null
    var name: String? = null
    var otherUserId: String? = null
    var otherUserImage: String? = null
    var lastMessage: String? = null
    var messages: MutableList<Message>? = null
}
