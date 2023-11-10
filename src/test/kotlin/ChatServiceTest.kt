import data.chat.Chat
import data.chat.Message
import exceptions.ChatNotFoundException
import exceptions.NoMessagesFoundException
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    val newChat = Chat(
        id = 1,
        messages = ArrayList(),
        participants = Pair(1,2)
    )

    val newMessage = Message(
        id = 1,
        text = "New Message",
        senderId = 1,
        receiverId = 2,
        isRead = false,
    )


    @Before
    fun prepare() {
        ChatService.clear()
    }

    @Test
    fun shouldCreateChat() {
        val result = ChatService.createChat(1, 2)
        Assert.assertEquals(newChat, result)
    }

    @Test
    fun shouldDeleteExistingChat() {
        val result = ChatService.createChat(1, 2)
        val res = ChatService.deleteChat(result.id)
        Assert.assertEquals(res, 1)
    }

    @Test
    fun shouldReturn0IfDeletedChatNotExists() {
        val res = ChatService.deleteChat(1)
        Assert.assertEquals(res, 0)
    }

    @Test
    fun shouldCreateNewMessageInExistingChat() {
        val chat = ChatService.createChat(1, 2)
        Assert.assertEquals(chat.messages.size, 0)
        val message = ChatService.createNewMessage(1, 2, "New Message")
        Assert.assertEquals(chat.messages.size, 1)
        Assert.assertEquals(newMessage, message)
    }

    @Test
    fun shouldCreateMessageAndChatIfChatNotExists() {
        val chats = ChatService.getChats(1)
        Assert.assertEquals(chats.size, 0)

        val message = ChatService.createNewMessage(1, 2, "New Message")
        Assert.assertEquals(newMessage, message)
        val chatsAfterCreatingMessage = ChatService.getChats(1)
        Assert.assertEquals(chatsAfterCreatingMessage.size, 1)
    }

    @Test
    fun shouldDeleteExistingMessage() {
        val message = ChatService.createNewMessage(1, 2, "New Message")
        val res = ChatService.deleteMessage(1, message.id)
        Assert.assertEquals(1, res)
    }

    @Test
    fun shouldReturn0IfChatWithMessageNotFound() {
        val res = ChatService.deleteMessage(1,1)
        Assert.assertEquals(0, res)
    }

    @Test
    fun shouldReturn0IfDeleterIsNotMemberOfChat() {
        ChatService.createNewMessage(1, 2, "New Message")
        val res = ChatService.deleteMessage(3,1)
        Assert.assertEquals(0, res)
    }

    @Test
    fun shouldReturnChats() {
        val chat1 = ChatService.createChat(1, 2)
        val chat2 = ChatService.createChat(2, 3)

        val chats = ChatService.getChats(2)
        Assert.assertEquals(chats.size, 2)
        Assert.assertEquals(chats[0], chat1)
        Assert.assertEquals(chats[1], chat2)
    }

    @Test
    fun shouldReturnUnreadChatsCount(){
        ChatService.createNewMessage(1, 2, "mes1")
        ChatService.createNewMessage(3, 2, "mes2")
        ChatService.createNewMessage(2, 3, "mes3")

        val res = ChatService.getUnreadChatsCount(2)
        Assert.assertEquals(2, res)
    }

    @Test
    fun shouldReturnLastChatMessages(){
        val mes1 = ChatService.createNewMessage(1, 2, "mes1")
        val mes2 = ChatService.createNewMessage(1, 2, "mes2")
        val mes3 = ChatService.createNewMessage(1, 2, "mes3")

        val chats = ChatService.getChats(1)
        val res = ChatService.getLastChatMessages(chats[0].id, 2)
        Assert.assertEquals(2, res.size)
        Assert.assertEquals(mes3, res[0])
        Assert.assertEquals(mes2, res[1])
    }

    @Test(expected = NoMessagesFoundException :: class)
    fun shouldThrowMessagesNotFoundExceptionForLastMessagesSearch() {
        val chat = ChatService.createChat(1, 2)
        ChatService.getLastChatMessages(chat.id, 2)
    }

    @Test(expected = ChatNotFoundException :: class)
    fun shouldThrowChatNotFoundExceptionForLastMessagesSearch() {
        ChatService.getLastChatMessages(1, 2)
    }

    @Test
    fun shouldReturnParticipantMessages(){
        val mes1 = ChatService.createNewMessage(1, 2, "mes1")
        val mes2 = ChatService.createNewMessage(1, 2, "mes2")
        val mes3 = ChatService.createNewMessage(1, 2, "mes3")

        val res = ChatService.getParticipantMessages(1, 2, 2)
        Assert.assertEquals(2, res.size)
        Assert.assertEquals(mes2, res[0])
        Assert.assertEquals(mes2.isRead, true)
        Assert.assertEquals(mes3, res[1])
        Assert.assertEquals(mes3.isRead, true)
    }

    @Test(expected = NoMessagesFoundException :: class)
    fun shouldThrowMessagesNotFoundExceptionForParticipantMessages() {
        ChatService.createChat(1, 2)
        ChatService.getParticipantMessages(1, 2, 2)
    }

    @Test(expected = ChatNotFoundException :: class)
    fun shouldThrowChatNotFoundExceptionForParticipantMessages() {
        ChatService.getParticipantMessages(1, 2, 2)
    }
}