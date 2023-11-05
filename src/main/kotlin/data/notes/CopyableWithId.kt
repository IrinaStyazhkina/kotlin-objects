package data.notes

interface CopyableWithId<T> {
    fun copy(newId: Int? = null, deletedStatus: Boolean? = null): T
    val id: Int
    val isDeleted: Boolean
}