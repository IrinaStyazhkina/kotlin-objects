package data.attachments

data class Sticker(
    val productId: Int,
    val stickerId: Int,
    val images: Array<StickerImages>,
    val imagesWithBackground: Array<StickerImages>,

)

data class StickerImages(
    val url: String,
    val width: Int,
    val height: Int,
)