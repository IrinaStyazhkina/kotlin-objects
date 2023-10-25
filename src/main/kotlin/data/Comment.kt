package data

import data.attachments.Attachment

data class Comment(
    val id: Int,
    val fromId: Int,
    val date: Int,
    val text: String,
    val donut: Donut,
    val replyToUser: Int?,
    val replyToComment: Int?,
    val attachments: Array<Attachment> = emptyArray<Attachment>(),
    val parentsStack: Array<Int> = emptyArray<Int>(),
    val thread: Thread,
)
