import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class AdjacencyMatrix(drawingApi: DrawingApi) : Graph(drawingApi) {
    private var adjacencyMatrix: MutableList<MutableList<Boolean>> = mutableListOf()

    override fun readGraph(file: File) {
        Scanner(FileInputStream(file)).use {
            vertexNumber = it.nextInt()
            adjacencyMatrix = MutableList(vertexNumber) { MutableList(vertexNumber) { false } }
            for (i in 0 until vertexNumber) {
                for (j in 0 until vertexNumber) {
                    adjacencyMatrix[i][j] = it.nextInt() != 0
                }
            }
        }
    }

    override var vertexNumber: Int = 0
    override fun drawLinks() {
        adjacencyMatrix.forEachIndexed { u, it ->
            it.forEachIndexed { v, value ->
                if (value && u < v) {
                    drawLink(u, v)
                }
            }
        }
    }

    override fun addLink(v: Int, u: Int) {
        adjacencyMatrix[u][v] = true
        adjacencyMatrix[v][u] = true
    }

    override fun addVertex() {
        vertexNumber++
        adjacencyMatrix.forEach {
            it.add(false)
        }
        adjacencyMatrix.add(MutableList(vertexNumber) { false })
    }
}