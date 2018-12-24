import org.joda.time.DateTime

data class Post(val id: Long, val date: DateTime)

const val MAX_REQUEST_COUNT = 50

interface Handler {
    suspend fun getDiagram(): Diagram?
}

class HandlerTimeStamp(
    private val downloader: Downloader<IdForNextPage.TimeStamp>,
    private val aggregator: Aggregator,
    private val hours: Int,
    private val tag: String
) : Handler {
    private val startTime = DateTime.now()

    private val lastTime
        get() = aggregator.lastPost?.date ?: startTime

    override suspend fun getDiagram(): Diagram? {
        val stopTime = startTime.minusHours(hours).minusMinutes(startTime.minuteOfHour)
        Logger.info("Stop time is $stopTime")

        var requestCounter = 0
        while (lastTime > stopTime && requestCounter++ <= MAX_REQUEST_COUNT) {
            val lastPosts = downloader.download(tag)
            aggregator.add(lastPosts)
            Logger.info("Now lastTime is $lastTime")

            if (lastPosts.size != downloader.countPost) {
                Logger.error("Vk api cannot give more info")
                break
            }
        }
        if (requestCounter > MAX_REQUEST_COUNT) return null
        return handleData()
    }

    private fun DateTime.toDayAndHour(): DayAndHour = DayAndHour(dayOfMonth().get(), hourOfDay().get())

    private fun handleData(): Diagram {
        val diagramWithoutZeroes = aggregator.sortedAll.groupBy { it.date.toDayAndHour() }
        var time = startTime.minusHours(hours)
        val diagram = mutableListOf<HourColumn>()
        for (i in 1..hours) {
            val curTime = time.toDayAndHour()
            val count = diagramWithoutZeroes.getOrDefault(curTime, listOf()).size
            diagram.add(HourColumn(curTime, count))
            time = time.plusHours(1)
        }
        return diagram
    }
}

data class DayAndHour(val day: Int, val hour: Int)

typealias Diagram = List<HourColumn>

data class HourColumn(val hour: DayAndHour, val count: Int)
