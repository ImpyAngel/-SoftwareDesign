import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class HandlerTest {
    private var post = Post.zero
    private val storage = mutableListOf<Post>()

    private fun postGenerator(count: Int): List<Post> =
        List(count) {
            ++post
        }

    private fun postGeneratorWithWrite(count: Int): List<Post> {
        val ans = List(count) {
            ++post
        }
        storage += ans
        return ans
    }

    @BeforeEach
    fun beforeEach() {
        post = Post.zero
        storage.clear()
    }

    @Test
    @DisplayName("Test with not mock Aggregator")
    fun notMock() {
        val mockDownloader = mock<Downloader<IdForNextPage.TimeStamp>> {
            on { runBlocking { download(any(), any()) } } doAnswer { postGenerator(50) }
            on { countPost } doReturn 50
        }

        val handler = HandlerTimeStamp(mockDownloader, AggregatorImpl(), 3, "NewYear")

        runBlocking {
            assertEquals(listOf(60, 60, 60), handler.getDiagram()!!.map { it.count })
        }
    }

    @Test
    @DisplayName("Test with all mock")
    fun allMock() {
        val mockDownloader = mock<Downloader<IdForNextPage.TimeStamp>> {
            on { runBlocking { download(any(), any()) } } doAnswer { postGeneratorWithWrite(50) }
            on { countPost } doReturn 50
        }

        val mockAggregator = mock<Aggregator> {
            on { sortedAll } doAnswer { storage }
            on { lastPost } doAnswer { post }
        }

        val handler = HandlerTimeStamp(mockDownloader, mockAggregator, 3, "NewYear")

        runBlocking {
            assertEquals(listOf(60, 60, 60), handler.getDiagram()!!.map { it.count })
        }
    }

}

