import data.*
import data.attachments.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {

    private val attachments = arrayOf<Attachment>(GraffitiAttachment(
        graffiti = Graffiti(
            id = 1,
            ownerId = 1,
            photo130 = "photo",
            photo604 = "photo",
        ),
    ))

    private val postToCreate = Post(
        ownerId = 1,
        fromId = 1,
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
        ),
        likes = Likes(
            count = 1,
            userLikes = true,
            canLike = true,
            canPublish = false,
        ),
        reposts = Reposts(
            count = 0,
            userReposted = false,
        ),
        views = Views(
            count = 6,
        ),
        postType = PostType.POST,
        postSource = PostSource(
            type = PostSourceType.VK,
            platform = Platform.ANDROID,
            data = PostSourceDataType.PROFILE_ACTIVITY,
            url = "url"
        ),
        geo = Geo(
            type = "geo",
            coordinates = "coordinates",
            place = Place(
                id = 1,
                title = "Home",
                latitude = 123,
                longitude = 123,
                created = 0,
                icon = "icon",
                checkins = 0,
                updated = 0,
                type = 1,
                country = 1,
                city = 1,
                address = "address",
            )
        ),
        signerId = 1,
        copyHistory = emptyArray<Post>(),
        postponedId = 1,
        replyOwnerId = null,
        replyPostId = null,
        attachments = attachments,
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