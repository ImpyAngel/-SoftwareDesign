import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer

class RxServer(private val driver: MongoDriver) {

    val allMethods = arrayOf(ApiMethod.AddItem(), ApiMethod.AddUser(), ApiMethod.Items())

    fun startServer() = HttpServer
            .newServer(8080)
            .start { req, resp ->
                val response = allMethods.firstOrNull { it.isCorrect(req) }?.execute(req, driver)
                if (response != null) {
                    resp.writeString(response)
                } else resp.setStatus(HttpResponseStatus.NOT_FOUND)
            }

}

fun main() {
    RxServer(MongoDriver()).startServer().awaitShutdown()
}
