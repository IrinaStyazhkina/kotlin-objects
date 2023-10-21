package data

data class PostSource(
    val type: PostSourceType,
    val platform: Platform,
    val data: PostSourceDataType?,
    val url: String,
)
