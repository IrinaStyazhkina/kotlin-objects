package data.attachments

sealed interface Attachment {
    val type: AttachmentType
}

data class PhotoAttachment(val photo: Photo): Attachment {
    override val type: AttachmentType = AttachmentType.PHOTO
}

data class StickerAttachment(val sticker: Sticker): Attachment {
    override val type: AttachmentType = AttachmentType.STICKER
}

data class GraffitiAttachment(val graffiti: Graffiti): Attachment {
    override val type: AttachmentType = AttachmentType.GRAFFITI
}

data class EventAttachment(val event: Event): Attachment {
    override val type: AttachmentType = AttachmentType.EVENT
}

data class NoteAttachment(val note: Note): Attachment {
    override val type: AttachmentType = AttachmentType.NOTE
}