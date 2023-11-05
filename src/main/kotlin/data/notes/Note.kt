package data.notes

data class Note(
    override val id: Int,
    override val isDeleted: Boolean = false,
    val title: String,
    val ownerId: Int,
    val createdDate: Int,
    val text: String,
    val privacy: Privacy,
    val commentPrivacy: Privacy,
    val privacyView: String,
    val privacyComment: String,
): CopyableWithId<Note> {
    override fun copy(newId: Int?, deletedStatus: Boolean?): Note = copy(
        id = newId ?: id,
        isDeleted = deletedStatus ?: isDeleted
    )
}
