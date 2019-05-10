import io.reactivex.netty.protocol.http.server.HttpServerRequest
import rx.Observable
import kotlin.math.round

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
sealed class ApiMethod() {

    protected operator fun <T> HttpServerRequest<T>.get(key: String): String = queryParameters[key]!![0]
    protected fun <T> HttpServerRequest<T>.hasParams(vararg keys: String) = keys.map { queryParameters.containsKey(it) }.all { it }

    protected abstract val prefix: String
    protected abstract val params: Array<String>
    abstract fun <T> execute(req: HttpServerRequest<T>, driver: MongoDriver): Observable<String>

    fun <T> isCorrect(req: HttpServerRequest<T>) = req.uri.startsWith(prefix) && req.hasParams(*params)

    class Items() : ApiMethod() {
        override val prefix = "/items"
        override val params = arrayOf("id")
        private fun justify(item: Item, cost: Double) = "Item - ${item.name} with cost - ${round(cost * 100) / 100}\n"

        override fun <T> execute(req: HttpServerRequest<T>, driver: MongoDriver): Observable<String> {
            val id = req["id"]
            val allItems = driver.items()
            val thisDude = driver.users().first { it.id == id }
            return thisDude.flatMap { dude ->
                allItems.map { item ->
                    val cost = item.cost / dude.currency.amout
                    justify(item, cost)
                }
            }
        }
    }

    class AddUser() : ApiMethod() {
        override val prefix = "/addUser"
        override val params = arrayOf("name", "id")

        override fun <T> execute(req: HttpServerRequest<T>, driver: MongoDriver): Observable<String> {
            val name = req["name"]
            val id = req["id"]
            val currency = req.queryParameters["currency"]?.let { Currency.valueOf(it[0]) } ?: Currency.RUB
            return driver.addUser(UserShop(name, id, currency)).map { it.name }
        }
    }

    class AddItem() : ApiMethod() {
        override val prefix = "/addItem"
        override val params = arrayOf("name", "cost")

        override fun <T> execute(req: HttpServerRequest<T>, driver: MongoDriver): Observable<String> {
            val name = req["name"]
            val cost = req["cost"].toInt()
            return driver.addItem(Item(name, cost)).map { it.name }
        }
    }
}
