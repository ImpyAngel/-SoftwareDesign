import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    if (args.size < 2 || args[1].toIntOrNull() == null) {
        println("Wrong input format")
    } else {
        runBlocking {
            val tag = "#${args[0]}"
            val size = args[1].toInt()
            val diagram = HandlerTimeStamp(DownloaderWithTimeStamp(DownloaderWithTimeStamp.httpClient(), Passwords()), AggregatorImpl(), size, tag).getDiagram()
            println(RepresentImpl().printDiagram(diagram))
        }
    }
}
