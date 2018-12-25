import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.response.readBytes
import kotlinx.io.charsets.decodeUtf8ResultCombine
import org.joda.time.DateTime
import java.io.FileInputStream
import java.lang.reflect.Type
import java.util.*

sealed class IdForNextPage {
    @Suppress("unused")
    data class Shift(val shift: Long): IdForNextPage()
    data class TimeStamp(var timeStamp: String): IdForNextPage()
}


interface Downloader<T: IdForNextPage> {
    val lastId: T?
    val countPost: Int
    suspend fun download(tag: String, restart: Boolean = false): List<Post>
}

open class Passwords {
    private val props = Properties()

    open fun password(): String? {
        return props.getProperty("password")
    }

    init {
        props.load(FileInputStream("src/main/resources/local.property"))
    }
}

class DownloaderWithTimeStamp(rowClient: HttpClient, private val passwords: Passwords) : Downloader<IdForNextPage.TimeStamp> {
    override val countPost = 200
    override var lastId: IdForNextPage.TimeStamp? = null

    private val secretKey get() = passwords.password()

    private var counter = 0

    class DateTimeSerializer : JsonDeserializer<DateTime> {
        override fun deserialize(unixTime: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime {
            return if (unixTime != null) DateTime(unixTime.asLong * 1000) else DateTime.now()
        }
    }
    private val client: HttpClient

    init {
        client = rowClient.config {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    registerTypeAdapter(DateTime::class.java, DateTimeSerializer())
                }
            }
        }
    }

    override suspend fun download(tag: String, restart: Boolean): List<Post> {
        counter++

        val response = client.get<Response>("https://api.vk.com/method/newsfeed.search") {
            parameter("access_token", secretKey)
            parameter("q", tag)
            parameter("count", countPost)
            parameter("v", 5.92)
            if (!restart) lastId?.let { parameter("start_from", it.timeStamp) }
        }.response ?: tagError("Deserialization problem, may be legacy token in password")

        Logger.info("Request #$counter")
            .add("Total download - ${response.items.size}")
            .add("Last Time - ${response.items.last().date}")

        Logger.debug {
            response.items.forEach {
                it.id.toString() + " " + it.date
            }
        }

        val nextFrom = response.nextFrom
        if (nextFrom != null) {
            lastId = IdForNextPage.TimeStamp(nextFrom)
        }

        return response.items
    }

    data class Items(val items: List<Post>, @SerializedName("next_from") var nextFrom: String?)
    class Response(val response: Items?)

    companion object {
        fun httpClient() = HttpClient()
    }
}

