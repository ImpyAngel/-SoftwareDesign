import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rx.observers.TestSubscriber
import kotlin.test.assertEquals


/**
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */


class MongoDriverKtTest {
    val driver = MongoDriver()


    @BeforeEach
    fun setUp() {
        driver.exterminate().subscribe()
    }

    @Test
    fun testAddUser() {
        driver.addUser(user1).subscribe {
            driver.addUser(user2).subscribe {
                val subscriber = TestSubscriber<UserShop>()
                driver.users().subscribe(subscriber)
                assertEquals(listOf(user1, user2), subscriber.onNextEvents)
            }
        }
    }

    @Test
    fun testAddItems() {
        driver.addItem(item1).subscribe {
            driver.addItem(item2).subscribe {
                val subscriber = TestSubscriber<Item>()
                driver.items().subscribe(subscriber)
                assertEquals(listOf(item1, item2), subscriber.onNextEvents)
            }
        }
    }
}