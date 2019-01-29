import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions
import java.util.*


/**
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class LruCacheTest {

    lateinit var cache: LruCache<Int, Int>

    @BeforeEach
    fun setUp() {
        cache = LruCache(10)
    }

    @Test
    fun insertGet() {
        for (i in 0..19) {
            cache.add(i, i)
            assertEquals(i, cache[i])
        }
    }

    @Test
    fun getNotExistingKey() {
        assertNull(cache[1])
    }

    @Test
    fun getPushedOutKey() {
        for (i in 0..19) {
            cache.add(i, i)
        }

        assertNull(cache[1])
    }

    @Test
    fun remove() {
        for (i in 0..8) {
            cache.add(i, i)
        }
        for (i in 0.. 4) {
            cache.remove(i * 2)
        }

        for (i in 0..8) {
            assertEquals(i.takeIf { i % 2 == 1 }, cache[i])
        }
    }
}