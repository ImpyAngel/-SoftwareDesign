import java.io.File
import java.io.IOException

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
fun main(args: Array<String>) {
    if (args.size < 3) {
        println("Format of input: <drawing api: awt|swing> <graph structure: list|matrix> <filepath to graph> <width>? <height>")
    } else {
        val width = args.getOrNull(4)?.toInt() ?: 600
        val height = args.getOrNull(5)?.toInt() ?: 600

        val algorithm: DrawingApi = if (args[0] == "awt") AwtDrawingApi(width, height) else SwingDrawingApi(width, height)
        val graph: Graph = if (args[1] == "matrix") AdjacencyMatrix(algorithm) else AdjacencyList(algorithm)
        try {
            graph.readGraph(File(args[2]))
        } catch (e: IOException) {
            println("Bad input")
            println(e.message)
        }
        graph.drawGraph()
    }

}
