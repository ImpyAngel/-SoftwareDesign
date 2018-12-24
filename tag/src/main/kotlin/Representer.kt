interface Represent {
    fun printDiagram(diagram: Diagram?): String
}

class RepresentImpl : Represent {
    private fun addZero(i: Int) : String = if (i < 10) "0$i" else "$i"

    private fun handleHour(i: Int): String {
        return addZero(i)+ ":00 - " +
                addZero((i + 1) % 24) + ":00"
    }


    override fun printDiagram(diagram: Diagram?): String =
        diagram?.joinToString("\n") { (date, count) ->
            addZero(date.day) + " " + handleHour(date.hour) + " -- " + count
        } ?: "Sorry, it is too often used tag :("

}