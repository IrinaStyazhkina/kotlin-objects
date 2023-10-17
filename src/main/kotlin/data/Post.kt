package data

data class Post(
    var id: Int = 0,
    val createdBy: Int,
    val date: Int,
    val text: String,
    val friendsOnly: Boolean,
    val comments: Comment,
    val copyright: Copyright,
    val canPin: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean,
    val isFavorite: Boolean,
)
