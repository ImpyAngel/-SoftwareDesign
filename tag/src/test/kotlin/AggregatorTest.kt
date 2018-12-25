import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AggregatorTest {

    @Test
    fun addBase() {
        var post = Post.zero

        val aggregator = AggregatorImpl()
        val posts = List(10) {
            ++post
        }
        assertEquals(null, aggregator.lastPost)
        aggregator.add(posts)
        assertEquals(post, aggregator.lastPost)

        val anotherList = List(10) {
            ++post
        }

        aggregator.add(anotherList)
        assertEquals(posts + anotherList, aggregator.sortedAll)
        assertEquals(post, aggregator.lastPost)

    }

    @Test
    fun addWithIntersection() {
        var post = Post.zero

        val aggregator = AggregatorImpl()
        val posts = List(10) {
            ++post
        }
        aggregator.add(posts)
        val intersection = posts.dropWhile { it.id < 5 }
        val nextPost = List(10) {
            ++post
        }
        aggregator.add(intersection + nextPost)
        assertEquals(posts + nextPost, aggregator.sortedAll)
    }
}