package data.chat

data class Chat(
    val id: Int,
    val messages: ArrayList<Message>,
    val participants: Pair<Int, Int>,
)
