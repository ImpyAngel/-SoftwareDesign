import java.io.File
import kotlin.math.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

abstract class Graph(protected val drawingGraph: DrawingGraph) {

    abstract fun drawLinks()

    private lateinit var coordinateCalculate: CoordinateCalculate
    abstract fun readGraph(file: File)

    abstract var vertexNumber: Int

    fun drawGraph() {
        coordinateCalculate = CoordinateCalculate(
            drawingGraph.drawingAreaWidth.toDouble(),
            drawingGraph.drawingAreaHeight.toDouble(),
            vertexNumber
        )
        drawVertexes()
        drawLinks()
        drawingGraph.drawAll()
    }

    abstract fun addLink(v: Int, u: Int)

    abstract fun addVertex()

    private fun drawVertexes() {
        for (i in 0 until vertexNumber) {
            drawingGraph.addCircle(i.toCoordinate(), coordinateCalculate.radiusVertex)
        }
    }

    private fun Int.toCoordinate(): Coordinate = coordinateCalculate.toCoordinate(this)

    protected fun drawLink(from: Int, to: Int) {
        drawingGraph.addLink(from.toCoordinate(), to.toCoordinate())
    }
}

data class Coordinate(val x: Double, val y: Double, val radiusOfVertex: Double) {
    fun distance(other: Coordinate) = sqrt(abs(x - other.x).pow(2) + abs(y - other.y).pow(2))
    fun centered(): Coordinate = this.copy(x + radiusOfVertex, y + radiusOfVertex)

}
