import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServer
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import rx.Observable


/**
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
internal class RxServerTest {

    companion object {
        lateinit var server: HttpServer<ByteBuf, ByteBuf>

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val mockDownloader = mock<MongoDriver> {
                on { items() } doReturn Observable.just(item1, item2)
                on { users() } doReturn Observable.just(user1, user2)
            }

            server = RxServer(mockDownloader).startServer()
        }
    }


    @Test
    fun testRubs() {
        val request = HttpGet("http://localhost:8080/items?id=1")
        HttpClientBuilder.create().build().execute(request).entity.content.run {
            val response = String(readAllBytes())
            assertEquals(response, goldRub)
        }
    }

    @Test
    fun testEuro() {
        val request = HttpGet("http://localhost:8080/items?id=2")
        HttpClientBuilder.create().build().execute(request).entity.content.run {
            val response = String(readAllBytes())
            assertEquals(response, goldEuro)
        }
    }
}