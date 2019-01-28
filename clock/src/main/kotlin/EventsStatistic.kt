import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
interface EventsStatistic {
    fun incEvent(name: String)
    fun eventStatisticByName(name: String): List<Int>
    fun getAllEventStatistic(): List<Pair<String, List<Int>>>
}

const val MINUTES = 60
const val SECONDS = 60
const val MILLS_ON_HOUR = 1000 * 60 * 60L
class EventsStatisticImplementation(val clock: Clock) : EventsStatistic {
    private val statistic: MutableMap<String, LinkedList<Instant>> = mutableMapOf()

    private fun clearStatistic(name: String, now: Instant) {
        val info = statistic[name]
        if (info != null) {
            while (!info.isEmpty() && info.last.plusMillis(MILLS_ON_HOUR).isBefore(now)) {
                info.pollLast()
            }
        }
    }

    override fun incEvent(name: String) {
        clearStatistic(name, clock.now)
        statistic.getOrPut(name) { LinkedList() }.addFirst(clock.now)
    }

    override fun eventStatisticByName(name: String) = eventStatisticByNameAndTime(name, clock.now)

    private fun eventStatisticByNameAndTime(name: String, now: Instant): List<Int> {
        clearStatistic(name, now)
        val info = statistic[name]
        val ans = MutableList(MINUTES) { 0 }

        info?.groupBy {
            floor((now.epochSecond - it.epochSecond).toDouble() / SECONDS)
        }?.forEach { (time, list) ->
            if (time < MINUTES) {
                ans[time.toInt()] = list.size
            }
        }
        return ans
    }

    override fun getAllEventStatistic(): List<Pair<String, List<Int>>> {
        val now = clock.now
        return statistic.keys.map {
            Pair(it, eventStatisticByNameAndTime(it, now))
        }
    }
}