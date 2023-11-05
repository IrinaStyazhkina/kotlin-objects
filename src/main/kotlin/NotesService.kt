import data.notes.Comment
import data.notes.CopyableWithId
import data.notes.Note
import exceptions.*
import kotlin.streams.toList

object NotesService {

    private var notes = ArrayList<Note>()
    private var lastNoteId = 0

    private var comments = ArrayList<Comment>()
    private var lastCommentId = 0

    fun addNote(newNote: Note): Int {
        return this.addEntity(newNote, notes, ++lastNoteId)
    }

    fun addComment(newComment: Comment): Int {
        if (newComment.message.length < 2) {
            throw ConstraintException("Минимальная длина комментария - 2 символа")
        }
        return this.addEntity(newComment, comments, ++lastCommentId)
    }

    fun editNote(editedEntity: Note): Int {
        val toUpdate = notes.find { it.id == editedEntity.id }
            ?: throw EntityNotFoundException("Сущность с таким id не существует")
        return this.editEntity(toUpdate, editedEntity, notes)
    }

    fun editComment(commentId: Int, ownerId: Int, message: String): Int {
        if (message.length < 2) {
            throw ConstraintException("Минимальная длина комментария - 2 символа")
        }
        val toUpdate =
            comments.find { it.id == commentId } ?: throw EntityNotFoundException("Сущность с таким id не существует")
        if (toUpdate.isDeleted) {
            throw EntityEditingForbiddenException("Нельзя редактировать удаленную сущность")
        }
        if (toUpdate.ownerId != ownerId) {
            throw AccessForbiddenException("Редактировать комментарий может только его владелец")
        }
        return this.editEntity(toUpdate, toUpdate.copy(message = message), comments)
    }

    fun deleteNote(noteId: Int): Int {
        val toDelete =
            notes.find { it.id == noteId } ?: throw EntityNotFoundException("Сущность с таким id не существует")
        return this.deleteEntity(toDelete, true, notes)
    }

    fun deleteComment(commentId: Int, ownerId: Int): Int {
        val toDelete =
            comments.find { it.id == commentId } ?: throw EntityNotFoundException("Сущность с таким id не существует")
        if (toDelete.ownerId != ownerId) {
            throw AccessForbiddenException("Удалить комментарий может только его владелец")
        }
        return this.deleteEntity(toDelete, totallyDelete = false, comments)
    }

    fun restoreComment(commentId: Int, ownerId: Int): Int {
        val toRestore =
            comments.find { it.id == commentId } ?: throw EntityNotFoundException("Сущность с таким id не существует")
        if (toRestore.ownerId != ownerId) {
            throw AccessForbiddenException("Восстановить комментарий может только его владелец")
        }
        if (toRestore.isDeleted) {
            comments[comments.indexOf(toRestore)] = toRestore.copy(deletedStatus = false)
        }
        return 1
    }

    fun getNotes(noteIds: Array<Int>, userId: Int, offset: Int, count: Int, sort: Int): List<Note> {
        return notes.stream()
            .filter { it.id in noteIds && it.ownerId == userId  }
            .sorted { o1, o2 ->
                if (sort == 1) {
                    o1.createdDate - o2.createdDate
                } else {
                    o2.createdDate - o1.createdDate
                }
            }
            .skip(offset.toLong())
            .limit(count.toLong())
            .toList()
    }

    fun getNoteById(noteId: Int, ownerId: Int): Note? {
        return notes.find { it.id == noteId && it.ownerId == ownerId }
    }

    fun getComments(noteId: Int, ownerId: Int, offset: Int, count: Int, sort: Int): List<Comment> {
        return comments.stream()
            .filter {it.noteId == noteId && it.ownerId == ownerId}
            .sorted { o1, o2 ->
                if (sort == 1) {
                    o1.createdDate - o2.createdDate
                } else {
                    o2.createdDate - o1.createdDate
                }
            }
            .skip(offset.toLong())
            .limit(count.toLong())
            .toList()
    }

    private fun <T : CopyableWithId<T>> addEntity(newEntity: T, entitiesList: ArrayList<T>, newId: Int): Int {
        val created = newEntity.copy(newId)
        entitiesList.add(created)
        return created.id
    }

    private fun <T : CopyableWithId<T>> editEntity(entity: T, editedEntity: T, entitiesList: ArrayList<T>): Int {
        entitiesList[entitiesList.indexOf(entity)] = editedEntity.copy()
        return 1
    }

    private fun <T : CopyableWithId<T>> deleteEntity(
        entity: T,
        totallyDelete: Boolean = false,
        entitiesList: ArrayList<T>
    ): Int {
        if (totallyDelete) {
            entitiesList.remove(entity)
        } else {
            if (entity.isDeleted) {
                throw EntityDeletingForbiddenException("Нельзя удалить уже удаленную сущность")
            }
            entitiesList[entitiesList.indexOf(entity)] = entity.copy(deletedStatus = true)
        }
        return 1
    }

    fun clear() {
        notes = ArrayList<Note>()
        comments = ArrayList<Comment>()
        lastNoteId = 0
        lastCommentId = 0
    }
}