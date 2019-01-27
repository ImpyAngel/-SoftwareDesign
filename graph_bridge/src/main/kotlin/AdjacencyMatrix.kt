/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class AdjacencyMatrixGraph(vertexNumber: Int, drawingGraph: DrawingGraph) : Graph(vertexNumber, drawingGraph) {
    private val adjacencyMatrix: MutableList<MutableList<Boolean>> = MutableList(vertexNumber) { MutableList(vertexNumber) { false } }

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