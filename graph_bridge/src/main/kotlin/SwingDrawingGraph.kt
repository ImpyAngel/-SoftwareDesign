import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import javax.swing.JComponent
import javax.swing.JFrame

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

class SwingDrawingApi(override val drawingAreaWidth: Int, override val drawingAreaHeight: Int) : DrawingApi,
    JFrame() {
    val figures: MutableSet<Figure> = mutableSetOf()

    override fun addCircle(coordinate: Coordinate, radius: Double) {
        figures.add(Figure.Circle(coordinate, radius))
    }

    override fun addLink(from: Coordinate, to: Coordinate) {
        figures.add(Figure.Link(from, to))
    }

    val window = JFrame()

    init {
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.setSize(drawingAreaWidth, drawingAreaHeight)
        window.isVisible = true
    }

    override fun drawAll() {
        window.contentPane.add(SwingGraph())
    }

    inner class SwingGraph : JComponent() {
        override fun paint(g: Graphics?) {
            val ga = g as Graphics2D
            ga.paint = Color.green
            figures.forEach {
                when (it) {
                    is Figure.Circle -> ga.fill(
                        Ellipse2D.Double(
                            it.center.x,
                            it.center.y,
                            it.radius * 2,
                            it.radius * 2
                        )
                    )
                    is Figure.Link -> {
                        val shifted = it.copy(it.from.centered(), it.to.centered())
                        ga.color = Color.black
                        ga.drawLine(
                            shifted.from.x.toInt(), shifted.from.y.toInt(),
                            shifted.to.x.toInt(), shifted.to.y.toInt()
                        )
                    }
                }
            }
        }
    }
}

