import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RepresentTest {

    val diagram = listOf(
        HourColumn(DayAndHour(1, 2), 2),
        HourColumn(DayAndHour(1, 3), 3),
        HourColumn(DayAndHour(2, 12), 22),
        HourColumn(DayAndHour(10, 20), 212)
    )

    val answer = """
        01 02:00 - 03:00 -- 2
        01 03:00 - 04:00 -- 3
        02 12:00 - 13:00 -- 22
        10 20:00 - 21:00 -- 212
    """.trimIndent()

    @Test
    fun baseTest() {
        val representedDiagram = RepresentImpl().printDiagram(diagram)
        assertEquals(answer, representedDiagram)
    }
}