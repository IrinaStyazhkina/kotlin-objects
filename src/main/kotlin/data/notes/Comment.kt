package data.notes

data class Comment(
    override val id: Int,
    override val isDeleted: Boolean = false,
    val ownerId: Int,
    val noteId: Int,
    val createdDate: Int,
    val replyTo: Int?,
    val message: String,
    val guid: String,
): CopyableWithId<Comment> {
    override fun copy(newId: Int?, deletedStatus: Boolean?): Comment = copy(
        id = newId ?: id,
        isDeleted = deletedStatus ?: isDeleted
    )
}
