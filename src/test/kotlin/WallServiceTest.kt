import data.Comment
import data.Copyright
import data.Post
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {

    private val postToCreate = Post(
        createdBy = 1,
        date = 0,
        text="Test post",
        friendsOnly = true,
        canDelete = false,
        canEdit = true,
        canPin = false,
        isFavorite = false,
        copyright = Copyright(
            id = 1,
            link = "link",
            name = "name",
            type = "type",
        ),
        comments = Comment(
            count = 3,
            canPost = true,
            groupsCanPost = false,
            canClose = false,
            canOpen = true,
        )
    )

    private val postToUpdate = postToCreate.copy(id = 1, friendsOnly = false)

    @Before
    fun prepare() {
        WallService.clear()
    }

    @Test
    fun shouldAddPost() {
        val addedPost = WallService.add(postToCreate)
        assertEquals(1, addedPost.id)
    }

    @Test
    fun shouldUpdateExistingPost() {
        WallService.add(postToCreate)
        val result = WallService.update(postToUpdate)
        assertEquals(true, result)
    }

    @Test
    fun shouldNotUpdateIfPostNotExists() {
        val result = WallService.update(postToUpdate)
        assertEquals(false, result)
    }
}