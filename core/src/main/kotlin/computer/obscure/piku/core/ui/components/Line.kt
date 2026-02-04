package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.classes.LineRun
import computer.obscure.piku.core.ui.components.props.LineProps
import kotlin.math.roundToInt

open class Line(
    override val props: LineProps
) : Component() {
    override val compType: CompType = CompType.LINE

    val runs = mutableListOf<LineRun>()

    override fun width(): Double {
        return 1.0
    }

    override fun height(): Double {
        return 1.0
    }
}

/**
 * Rebuilds the cached pixel geometry for the [Line].
 *
 * Converts the line into a list of axis aligned [LineRun]s using
 * a line algorithm (Bresenham)
 *
 * This method serves to optimise the original line drawer by computing the
 * geometry once, caching it, and reusing it until the line changes (is marked dirty)
 *
 * This method preserves the pixelated look of lines that are lost by rotating a rectangle,
 * which was the original method in the client-side LineRenderer.
 */
fun Line.rebuildRuns() {
    runs.clear()

    var x0 = (props.from.x - props.pos.x).roundToInt()
    var y0 = (props.from.y - props.pos.y).roundToInt()
    val x1 = (props.to.x - props.pos.x).roundToInt()
    val y1 = (props.to.y - props.pos.y).roundToInt()

    val thickness = props.pointSize.y.roundToInt().coerceAtLeast(1)
    val half = thickness / 2

    val dx = kotlin.math.abs(x1 - x0)
    val dy = kotlin.math.abs(y1 - y0)
    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1
    var err = dx - dy

    var runX = x0
    var runY = y0

    while (true) {
        val e2 = err * 2
        val prevX = x0
        val prevY = y0

        if (e2 > -dy) { err -= dy; x0 += sx }
        if (e2 < dx) { err += dx; y0 += sy }

        if (x0 != prevX && y0 != prevY) {
            runs.add(LineRun(
                runX,
                runY - half,
                prevX - runX + 1,
                thickness
            ))
            runX = x0
            runY = y0
        }

        if (x0 == x1 && y0 == y1) break
    }

    runs.add(LineRun(
        runX,
        runY - half,
        x1 - runX + 1,
        thickness
    ))

    props.geometryDirty = false
}