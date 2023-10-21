package data.attachments

sealed interface Attachment {
    val type: AttachmentType
}

data class PhotoAttachment(override val type: AttachmentType = AttachmentType.PHOTO, val photo: Photo): Attachment

data class StickerAttachment(override val type: AttachmentType = AttachmentType.STICKER, val sticker: Sticker): Attachment

data class GraffitiAttachment(override val type: AttachmentType = AttachmentType.GRAFFITI, val graffiti: Graffiti): Attachment

data class EventAttachment(override val type: AttachmentType = AttachmentType.EVENT, val event: Event): Attachment

data class NoteAttachment(override val type: AttachmentType = AttachmentType.NOTE, val note: Note): Attachment