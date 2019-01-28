import java.lang.Double.min
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

class CoordinateCalculate(width: Double, height: Double, private var vertexNumber: Int) {
    private val center = min(width, height) / 2
    private val radius = (center * 0.8) / (1 + 2 * PI / vertexNumber * 0.8)
    val radiusVertex = (center * 0.8 - radius) / 2
    fun toCoordinate(index: Int): Coordinate {
        val phi = (index.toDouble() / vertexNumber) * 2 * PI
        val x = cos(phi) * radius + center
        val y = sin(phi) * radius + center
        return Coordinate(x, y, radiusVertex)
    }
}