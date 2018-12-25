import com.nhaarman.mockitokotlin2.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.FileInputStream

class DownloaderWithTimeStampTest {

    private val mockPasswords = mock<Passwords> {
        on { password() } doReturn "password"
    }

    @Test
    @DisplayName("Test on good real json")
    fun positive() {
        val realAnswer = FileInputStream("src/test/resources/realGoodAnswer.json").readBytes()

        val httpMockEngine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK,
                ByteReadChannel(realAnswer),
                headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
            )
        }
        val mockClient = HttpClient(httpMockEngine)

        val downloader = DownloaderWithTimeStamp(mockClient, mockPasswords)
        val posts = runBlocking {
            downloader.download("Учеба")
        }
        assertEquals(listOf(5353L, 5753L), posts.map { it.id })
        assertEquals(IdForNextPage.TimeStamp("2/-163952244_5753"), downloader.lastId)
    }

    @Test
    @DisplayName("Test on bad real json")
    fun negative() {
        val realBadAnswer = FileInputStream("src/test/resources/realAnswerWithBadToken.json").readBytes()
        val httpMockEngine = MockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK,
                ByteReadChannel(realBadAnswer),
                headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
            )
        }
        val mockClient = HttpClient(httpMockEngine)

        val downloader = DownloaderWithTimeStamp(mockClient, mockPasswords)
        assertThrows(TagException::class.java) {
            runBlocking {
                downloader.download("Учеба")
            }
        }
    }
}