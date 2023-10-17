import data.Post

object WallService {
    private var posts = emptyArray<Post>()
    private var lastId = 0

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

    fun clear() {
        posts = emptyArray<Post>()
        lastId = 0
    }
}