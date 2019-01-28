import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class EventStatisticTest {

    @Test
    fun testHalfTime() {
        val now = Instant.now()
        val name = "RELOAD"
        val settableClock = SettableClock(now)
        val statistic = EventsStatisticImplementation(settableClock)
        for (i in 0..MINUTES / 2) {
            statistic.incEvent(name)
            statistic.incEvent(name)
            statistic.incEvent(name)
            settableClock.now = settableClock.now.plusSeconds(MINUTES * 2L)
        }
        val expected = List(MINUTES) {
            if (it % 2 == 0 && it != 0) 3 else 0
        }
        assertEquals(expected, statistic.eventStatisticByName(name))
    }

    @Test
    fun testTwoAction() {
        val now = Instant.now()
        val name1 = "RELOAD"
        val name2 = "TERMINATE"
        val settableClock = SettableClock(now)
        val statistic = EventsStatisticImplementation(settableClock)
        for (i in 0 until 1000) {
            statistic.incEvent(name1)
            settableClock.now = settableClock.now.plusMillis(1000)
            statistic.incEvent(name2)
            settableClock.now = settableClock.now.plusMillis(1000)
        }
        val statistic1 = statistic.eventStatisticByName(name1)
        val statistic2 = statistic.eventStatisticByName(name2)
        assertEquals(1000, statistic1.sum())

        assertEquals(1000, statistic2.sum())

        assertEquals(statistic1 + statistic2, statistic.getAllEventStatistic().flatMap { it.second })
    }
}