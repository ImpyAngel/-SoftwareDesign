import java.awt.Color
import java.awt.Color.black
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */


interface DrawingGraph {
    val drawingAreaWidth: Int
    val drawingAreaHeight: Int

    fun addCircle(coordinate: Coordinate, radius: Double)
    fun addLink(from: Coordinate, to: Coordinate)
    fun drawAll()
}


sealed class Figure {
    data class Circle(val center: Coordinate, val radius: Double) : Figure()
    data class Link(val from: Coordinate, val to: Coordinate) : Figure()
}

class AwtGraphDrawing(override val drawingAreaWidth: Int, override val drawingAreaHeight: Int) : Frame(), DrawingGraph {
    val figures: MutableSet<Figure> = mutableSetOf()

    override fun drawAll() {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent?) {
                System.exit(0)
            }
        })
        setSize(drawingAreaWidth, drawingAreaHeight)
        isVisible = true
    }

    override fun paint(g: Graphics?) {
        val ga = g as Graphics2D
        ga.paint = Color.green
        figures.forEach {
            when (it) {
                is Figure.Circle -> ga.fill(Ellipse2D.Double(it.center.x, it.center.y, it.radius * 2, it.radius * 2))
                is Figure.Link -> {
                    val shifted = it.copy(it.from.centered(), it.to.centered())
                    ga.color = black
                    ga.drawLine(
                        shifted.from.x.toInt(), shifted.from.y.toInt(),
                        shifted.to.x.toInt(), shifted.to.y.toInt()
                    )
                }
            }
        }
    }

    override fun addCircle(coordinate: Coordinate, radius: Double) {
        figures.add(Figure.Circle(coordinate, radius))
    }

    override fun addLink(from: Coordinate, to: Coordinate) {
        figures.add(Figure.Link(from, to))
    }
}




//    val awt = AwtGraphDrawing(600, 600)
//    val awt = SwingDrawingGraph(600, 600)
//    val graph = AdjacencyListGraph(10, awt)
//    graph.addLink(0, 2)
//    graph.addLink(0, 4)
//    graph.addLink(3, 1)
//    graph.drawGraph()

