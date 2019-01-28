import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */


class AdjacencyList(drawingApi: DrawingApi) : Graph(drawingApi) {
    override fun readGraph(file: File) {
        Scanner(FileInputStream(file)).use {
            vertexNumber = it.nextInt()
            adjacencyList = MutableList(vertexNumber) { mutableListOf<Int>() }
            for (i in 0 until vertexNumber) {
                val size = it.nextInt()
                for (j in 0 until size) {
                    adjacencyList[i].add(it.nextInt())
                }
            }
        }
    }

    override var vertexNumber: Int = 0

    private var adjacencyList: MutableList<MutableList<Int>> = MutableList(vertexNumber) { mutableListOf<Int>() }

    override fun addLink(v: Int, u: Int) {
        if (!adjacencyList[v].contains(u)) {
            adjacencyList[v].add(u)
            adjacencyList[u].add(v)
        } else {
            error("This link already created")
        }
    }

    override fun addVertex() {
        vertexNumber++
        adjacencyList.add(mutableListOf())
    }

    override fun drawLinks() {
        adjacencyList.forEachIndexed { index, links ->
            links.forEach {
                if (index < it)
                    drawLink(index, it)
            }
        }
    }
}