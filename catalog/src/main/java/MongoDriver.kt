import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import org.bson.Document
import rx.Observable

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

open class MongoDriver {
    private fun client() = createMongoClient()

    private fun createMongoClient() = MongoClients.create("mongodb://localhost:27017")

    private fun database() = client().getDatabase("shop")

    private fun usersCollection() = database().getCollection("users")

    private fun itemsCollection() = database().getCollection("items")

    fun exterminate() = database().drop()

    open fun users() = usersCollection().find().toObservable().map(::UserShop)

    open fun items() = itemsCollection().find().toObservable().map(::Item)

    fun addUser(user: UserShop): Observable<Success> = usersCollection().insertOne(user.toDocument())

    fun addItem(item: Item): Observable<Success> = itemsCollection().insertOne(item.toDocument())
}

fun main() {
    val driver = MongoDriver()
    driver.users().subscribe(::println)
    Thread.sleep(5000)
    driver.addUser(UserShop("13", "Boben", Currency.RUB))
    driver.users().subscribe {
        println("$it!")
    }
    Thread.sleep(10000)
}

enum class Currency(val amout: Double) {
    RUB(1.0), EURO(73.66), DOLLAR(65.58)
}


data class Item(val name: String, val cost: Int) {
    constructor(document: Document) : this(document["name"] as String, document["cost"] as Int)

    fun toDocument(): Document = Document()
            .append("name", name)
            .append("cost", cost)
}

data class UserShop(val id: String, val name: String, val currency: Currency) {
    constructor(document: Document) : this(document["id"] as String, document["name"] as String, Currency.valueOf(document["currency"].toString()))

    fun toDocument(): Document = Document()
            .append("id", id)
            .append("name", name)
            .append("currency", currency.name)
}

