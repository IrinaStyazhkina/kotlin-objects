import data.chat.Chat
import data.chat.Message
import exceptions.ChatNotFoundException
import exceptions.NoMessagesFoundException

object ChatService {

    private var chats = ArrayList<Chat>()
    private var lastChatId = 0
    private var lastMessageId = 0


    fun getChats(participantId: Int): List<Chat> {
        return chats.filter { it.participants.first == participantId || it.participants.second == participantId }
    }

    fun getUnreadChatsCount(participantId: Int): Int {
        return chats
            .asSequence()
            .filter { it.participants.first == participantId || it.participants.second == participantId }
            .count { it.messages.any { it.receiverId == participantId && !it.isRead } }
    }

    fun getLastChatMessages(chatId: Int, messagesCount: Int): List<Message> {
        return chats.singleOrNull { it.id == chatId }
            .let { it?.messages ?: throw ChatNotFoundException("Чат с таким id не существует") }
            .reversed()
            .asSequence()
            .take(messagesCount)
            .ifEmpty { throw NoMessagesFoundException("Нет сообщений") }
            .toList()
    }

    fun getParticipantMessages(participantId: Int, partnerId: Int, count: Int): List<Message> {
        return chats.singleOrNull{ (it.participants.first == participantId && it.participants.second == partnerId) ||
                (it.participants.second == participantId && it.participants.first == partnerId)}
            .let {it?.messages ?: throw ChatNotFoundException("Такой чат не существует")}
            .reversed()
            .asSequence<Message>()
            .take(count)
            .ifEmpty { throw NoMessagesFoundException("Нет сообщений") }
            .map{ it.isRead = true; it.copy(isRead = true) }
            .toList()
            .reversed()
    }

    fun createNewMessage(from: Int, to: Int, text: String): Message {
        val message = Message(id = ++ lastMessageId, text, senderId = from, receiverId = to)

        chats.singleOrNull(){ (it.participants.first == from && it.participants.second == to) ||
                (it.participants.second == to && it.participants.first == from)}
            .let {it?.messages?.add(message) ?: createChat(from, to).messages.add(message)}
        return message
    }

    fun deleteMessage(participantId: Int, messageId: Int): Int {
        val res = chats
            .asSequence()
            .filter { it.participants.first == participantId || it.participants.second == participantId }
            .singleOrNull{ it.messages.any{ it.id == messageId } }
            ?.messages
            ?.removeIf { it.id == messageId }
        return if(res == true) 1 else 0
    }
    fun deleteChat(chatId: Int): Int {
        val res = chats.removeIf { it.id == chatId }
        return if(res) 1 else 0
    }

    fun createChat(from: Int, to: Int): Chat {
        val newChat = Chat(id = ++ lastChatId, messages = ArrayList(), participants = Pair(from,to))
        chats.add(newChat)
        return newChat
    }

    fun clear() {
        chats = ArrayList<Chat>()
        lastChatId = 0
        lastMessageId = 0
    }
}