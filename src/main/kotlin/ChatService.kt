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
        return getChats(participantId).count { it.messages.any { it.receiverId == participantId && !it.isRead } }
    }

    fun getLastChatMessages(chatId: Int, messagesCount: Int): List<Message> {
        val chat = chats.find { it.id == chatId } ?: throw ChatNotFoundException("Чат с таким id не существует")
        val messages =  chat.messages.takeLast(messagesCount)
        if(messages.isEmpty()) throw NoMessagesFoundException("Нет сообщений")
        return messages
    }

    fun getParticipantMessages(participantId: Int, partnerId: Int, count: Int): List<Message> {
        val chat = chats.find {
            (it.participants.first == participantId && it.participants.second == partnerId) ||
                    (it.participants.second == participantId && it.participants.first == partnerId)
        }
            ?: throw ChatNotFoundException("Такой чат не существует")

        val messages =  chat.messages.takeLast(count)
        if(messages.isEmpty()) throw NoMessagesFoundException("Нет сообщений")
        messages.map { it.isRead = true }
        return messages
    }

    fun createNewMessage(from: Int, to: Int, text: String): Message {
        val message = Message(id = ++ lastMessageId, text, senderId = from, receiverId = to)
        val chat = chats.find {
            (it.participants.first == from && it.participants.second == to) ||
                    (it.participants.second == to && it.participants.first == from)
        } ?: createChat(from, to)
        chat.messages.add(message)
        return message
    }

    fun deleteMessage(participantId: Int, messageId: Int): Int {
        val chat = chats.find{
            it.messages.any{ it.id == messageId }
        } ?: return 0
        if(chat.participants.first == participantId || chat.participants.second == participantId) {
            chat.messages.removeIf { it.id == messageId }
            return 1
        }
        return 0
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