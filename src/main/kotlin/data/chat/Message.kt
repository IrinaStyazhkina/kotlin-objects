package data.chat

data class Message(
    val id: Int,
    val text: String,
    val senderId: Int,
    val receiverId: Int,
    var isRead: Boolean = false,
)
