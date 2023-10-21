package data

enum class PostSourceDataType(val type: String) {
    PROFILE_ACTIVITY("profile_activity"),
    PROFILE_PHOTO("profile_photo"),
    COMMENTS("comments"),
    LIKE("like"),
    POLL("poll"),
}