import java.io.File
import kotlin.math.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

abstract class Graph(protected val drawingApi: DrawingApi) {

    abstract fun drawLinks()

    private lateinit var coordinateCalculate: CoordinateCalculate
    abstract fun readGraph(file: File)

    abstract var vertexNumber: Int

    fun drawGraph() {
        coordinateCalculate = CoordinateCalculate(
            drawingApi.drawingAreaWidth.toDouble(),
            drawingApi.drawingAreaHeight.toDouble(),
            vertexNumber
        )
        drawVertexes()
        drawLinks()
        drawingApi.drawAll()
    }

    abstract fun addLink(v: Int, u: Int)

    abstract fun addVertex()

    private fun drawVertexes() {
        for (i in 0 until vertexNumber) {
            drawingApi.addCircle(i.toCoordinate(), coordinateCalculate.radiusVertex)
        }
    }

    private fun Int.toCoordinate(): Coordinate = coordinateCalculate.toCoordinate(this)

    protected fun drawLink(from: Int, to: Int) {
        drawingApi.addLink(from.toCoordinate(), to.toCoordinate())
    }
}

data class Coordinate(val x: Double, val y: Double, val radiusOfVertex: Double) {
    fun centered(): Coordinate = this.copy(x + radiusOfVertex, y + radiusOfVertex)
}
