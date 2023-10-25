import data.Comment
import data.CommentReport
import data.Post
import exceptions.CommentNotFoundException
import exceptions.IncorrectReasonException
import exceptions.PostNotFoundException

object WallService {
    private var posts = emptyArray<Post>()
    private var lastId = 0
    private var comments = emptyArray<Comment>()
    private var reports = emptyArray<CommentReport>()

    fun add(post: Post): Post {
        posts += post.copy(id = ++lastId)
        return posts.last()
    }

    fun update(post: Post): Boolean {
        val toUpdate = posts.find { it.id == post.id }
        if (toUpdate != null) {
            posts[posts.indexOf(toUpdate)] = post
            return true
        }
        return false
    }

    fun createComment(postId: Int, comment: Comment): Comment {
        val postToUpdate = posts.find { it.id == postId } ?: throw PostNotFoundException("Поста с таким id не существует")
        comments += comment
        return comments.last()
    }

    fun reportComment(ownerId: Int, commentId: Int, reason: Int): Int {
        val reportedComment = comments.find { it.id == commentId } ?: throw CommentNotFoundException("Комментария с таким id не существует")
        if(reason in 1 .. 8) {
            reports += CommentReport(
                ownerId,
                commentId,
                reason
            )
            return 1
        } else {
            throw IncorrectReasonException("Некорректная причина жалобы")
        }


    }

    fun clear() {
        posts = emptyArray<Post>()
        comments = emptyArray<Comment>()
        reports = emptyArray<CommentReport>()
        lastId = 0
    }
}