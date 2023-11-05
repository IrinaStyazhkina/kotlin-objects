import data.notes.Comment
import data.notes.Note
import data.notes.Privacy
import exceptions.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NotesServiceTest {

    private val noteToAdd = Note(
        id = 0,
        privacyComment = "privacyComment",
        ownerId = 1,
        text = "text",
        createdDate = 12,
        title = "title",
        commentPrivacy = Privacy.ALL_USERS,
        privacy = Privacy.ALL_USERS,
        privacyView = "privacyView",
    )

    private val noteToUpdate = noteToAdd.copy(text = "newText", id = 1)


    private val commentToAdd = Comment(
        id = 0,
        ownerId = 1,
        noteId = 1,
        createdDate = 12,
        replyTo = 2,
        message = "message",
        guid = "234bhjbj-jkbjk32-djknks",
    )

    private val incorrectComment = commentToAdd.copy(message = "1")

    @Before
    fun prepare() {
        NotesService.clear()
    }

    @Test
    fun shouldAddNote() {
        val result = NotesService.addNote(noteToAdd)
        Assert.assertEquals(1, result)
    }

    @Test
    fun shouldAddComment() {
        NotesService.addNote(noteToAdd)
        val commentId = NotesService.addComment(commentToAdd)
        Assert.assertEquals(1, commentId)
    }

    @Test(expected = ConstraintException:: class)
    fun shouldNotAddCommentIfItsLengthLess2Characters() {
        NotesService.addNote(noteToAdd)
        NotesService.addComment(incorrectComment)
    }

    @Test
    fun shouldEditNote() {
        NotesService.addNote(noteToAdd)
        val result = NotesService.editNote(noteToUpdate)
        Assert.assertEquals(1, result)
    }

    @Test(expected = EntityNotFoundException::class)
    fun shouldThrowIfEditedNoteIsNotFound() {
        NotesService.editNote(noteToUpdate)
    }

    @Test
    fun shouldEditComment() {
        NotesService.addNote(noteToAdd)
        val commentId = NotesService.addComment(commentToAdd)
        val result = NotesService.editComment(commentId, 1, "mes2")
        Assert.assertEquals(1, result)
    }

    @Test(expected = ConstraintException:: class)
    fun shouldNotEditCommentIfItsLengthLess2Characters() {
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd)
        NotesService.editComment(1, 1, "1")
    }

    @Test(expected = EntityNotFoundException::class)
    fun shouldThrowIfEditedCommentIsNotFound() {
        NotesService.editComment(1, 1, "message1")
    }

    @Test(expected = EntityEditingForbiddenException::class)
    fun shouldThrowIfEditedCommentIsDeleted() {
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(isDeleted = true))
        NotesService.editComment(1, 1, "message1")
    }

    @Test(expected = AccessForbiddenException::class)
    fun shouldThrowIfEditorOfCommentIsNotOwner() {
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd)
        NotesService.editComment(1, 2, "message1")
    }

    @Test
    fun shouldDeleteNote() {
        val noteId = NotesService.addNote(noteToAdd)
        val result = NotesService.deleteNote(noteId)
        Assert.assertEquals(1, result)
    }

    @Test(expected = EntityNotFoundException :: class)
    fun shouldThrowIfDeleteNoteIsNotFound() {
        NotesService.addNote(noteToAdd)
        NotesService.deleteNote(2)
    }

    @Test
    fun shouldDeleteComment() {
        NotesService.addNote(noteToAdd)
        val commentId = NotesService.addComment(commentToAdd)
        val result = NotesService.deleteComment(commentId, 1)
        Assert.assertEquals(1, result)
    }

    @Test(expected = EntityNotFoundException::class)
    fun shouldThrowIfDeletedCommentIsNotFound() {
        NotesService.deleteComment(1, 1)
    }

    @Test(expected = AccessForbiddenException::class)
    fun shouldThrowIfDeleterOfCommentIsNotOwner() {
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd)
        NotesService.deleteComment(1, 2)
    }

    @Test(expected = EntityDeletingForbiddenException :: class)
    fun shouldThrowIfCommentIsAlreadyDeleted() {
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(isDeleted = true))
        NotesService.deleteComment(1, 1)
    }

    @Test
    fun shouldRestoreComment() {
        NotesService.addNote(noteToAdd)
        val commentId = NotesService.addComment(commentToAdd)
        NotesService.deleteComment(commentId, 1)
        val result = NotesService.restoreComment(commentId, 1)
        Assert.assertEquals(1, result)
    }

    @Test(expected = EntityNotFoundException::class)
    fun shouldThrowIfRestoredCommentIsNotFound() {
        NotesService.restoreComment(1, 1)
    }

    @Test(expected = AccessForbiddenException::class)
    fun shouldThrowIfRestorerOfCommentIsNotOwner() {
        NotesService.addNote(noteToAdd)
        val commentId = NotesService.addComment(commentToAdd)
        NotesService.deleteComment(commentId, 1)
        NotesService.restoreComment(commentId, 2)
    }

    @Test
    fun shouldGetNotes() {
        val res1 = noteToAdd.copy(createdDate = 1, id = 1)
        val res2 = noteToAdd.copy(createdDate = 2, id =2)
        NotesService.addNote(noteToAdd.copy(createdDate = 1))
        NotesService.addNote(noteToAdd.copy(createdDate = 2))
        val result = NotesService.getNotes(arrayOf(1,2), 1, 0, 5, 1)
        assertEquals(2, result.size)
        assertEquals(res1, result[0])
        assertEquals(res2, result[1])
    }

    @Test
    fun shouldSortNotesDescending() {
        val res2 = noteToAdd.copy(createdDate = 1, id = 1)
        val res1 = noteToAdd.copy(createdDate = 2, id =2)
        NotesService.addNote(noteToAdd.copy(createdDate = 1))
        NotesService.addNote(noteToAdd.copy(createdDate = 2))
        val result = NotesService.getNotes(arrayOf(1,2), 1, 0, 5, 0)
        assertEquals(2, result.size)
        assertEquals(res1, result[0])
        assertEquals(res2, result[1])
    }

    @Test
    fun shouldGetLimitedAmountOfNotes() {
        val res1 = noteToAdd.copy(createdDate = 1, id = 1)
        NotesService.addNote(noteToAdd.copy(createdDate = 1))
        NotesService.addNote(noteToAdd.copy(createdDate = 2))
        val result = NotesService.getNotes(arrayOf(1,2), 1, 0, 1, 1)
        assertEquals(1, result.size)
        assertEquals(res1, result[0])
    }

    @Test
    fun shouldGetOffsetNotes() {
        val res2 = noteToAdd.copy(createdDate = 2, id =2)
        NotesService.addNote(noteToAdd.copy(createdDate = 1))
        NotesService.addNote(noteToAdd.copy(createdDate = 2))
        val result = NotesService.getNotes(arrayOf(1,2), 1, 1, 1, 1)
        assertEquals(1, result.size)
        assertEquals(res2, result[0])
    }

    @Test
    fun shouldGetOnlyOwnerNotes() {
        val res1 = noteToAdd.copy(createdDate = 1, id = 1)
        NotesService.addNote(noteToAdd.copy(createdDate = 1))
        NotesService.addNote(noteToAdd.copy(createdDate = 2, ownerId = 2))
        val result = NotesService.getNotes(arrayOf(1,2), 1, 0, 5, 1)
        assertEquals(1, result.size)
        assertEquals(res1, result[0])
    }

    @Test
    fun shouldGetNoteById() {
        val res1 = noteToAdd.copy(createdDate = 1, id = 1)
        NotesService.addNote(noteToAdd.copy(createdDate = 1))
        val result = NotesService.getNoteById(1,1)
        assertEquals(res1, result)
    }

    @Test
    fun shouldNotReturnNoteByIdForNotOwner() {
        NotesService.addNote(noteToAdd.copy(createdDate = 1, ownerId = 2))
        val result = NotesService.getNoteById(1,1)
        assertEquals(null, result)
    }

    @Test
    fun shouldGetComments() {
        val res1 = commentToAdd.copy(createdDate = 1, id = 1)
        val res2 = commentToAdd.copy(createdDate = 2, id = 2)
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(createdDate = 1))
        NotesService.addComment(commentToAdd.copy(createdDate = 2))
        val result = NotesService.getComments(1, 1, 0, 5, 1)
        assertEquals(2, result.size)
        assertEquals(res1, result[0])
        assertEquals(res2, result[1])
    }

    @Test
    fun shouldSortCommentsDescending() {
        val res2 = commentToAdd.copy(createdDate = 1, id = 1)
        val res1 = commentToAdd.copy(createdDate = 2, id = 2)
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(createdDate = 1))
        NotesService.addComment(commentToAdd.copy(createdDate = 2))
        val result = NotesService.getComments(1, 1, 0, 5, 0)
        assertEquals(2, result.size)
        assertEquals(res1, result[0])
        assertEquals(res2, result[1])
    }

    @Test
    fun shouldGetLimitedAmountOfComments() {
        val res1 = commentToAdd.copy(createdDate = 1, id = 1)
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(createdDate = 1))
        NotesService.addComment(commentToAdd.copy(createdDate = 2))
        val result = NotesService.getComments(1, 1, 0, 1, 1)
        assertEquals(1, result.size)
        assertEquals(res1, result[0])
    }

    @Test
    fun shouldGetOffsetComments() {
        val res2 = commentToAdd.copy(createdDate = 2, id = 2)
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(createdDate = 1))
        NotesService.addComment(commentToAdd.copy(createdDate = 2))
        val result = NotesService.getComments(1, 1, 1, 1, 1)
        assertEquals(1, result.size)
        assertEquals(res2, result[0])
    }

    @Test
    fun shouldGetOnlyOwnerComments() {
        val res1 = commentToAdd.copy(createdDate = 1, id = 1)
        NotesService.addNote(noteToAdd)
        NotesService.addComment(commentToAdd.copy(createdDate = 1))
        NotesService.addComment(commentToAdd.copy(createdDate = 2, ownerId = 2))
        val result = NotesService.getComments(1, 1, 0, 5, 0)
        assertEquals(1, result.size)
        assertEquals(res1, result[0])
    }
}