package data.notes

enum class Privacy(val value: Int) {
    ALL_USERS(0),
    ONLY_FRIENDS(1),
    FRIENDS_OF_FRIENDS(2),
    ONLY_USER(3),
}