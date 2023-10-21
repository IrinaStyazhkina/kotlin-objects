package data.attachments

enum class AttachmentType(val type: String) {
    PHOTO("photo"),
    STICKER("sticker"),
    EVENT("event"),
    NOTE("note"),
    GRAFFITI("graffiti"),
}