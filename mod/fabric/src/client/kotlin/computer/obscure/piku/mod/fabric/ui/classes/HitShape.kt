package computer.obscure.piku.mod.fabric.ui.classes

sealed interface HitShape {
    fun contains(x: Float, y: Float, bounds: ShapeBounds): Boolean

    fun outlinePoints(bounds: ShapeBounds): List<Pair<Float, Float>>

    data object Rectangle : HitShape {
        override fun contains(x: Float, y: Float, bounds: ShapeBounds): Boolean {
            return x >= bounds.x && x <= bounds.x + bounds.width &&
                    y >= bounds.y && y <= bounds.y + bounds.height
        }

        override fun outlinePoints(bounds: ShapeBounds): List<Pair<Float, Float>> = listOf(
            bounds.x to bounds.y,
            bounds.x + bounds.width to bounds.y,
            bounds.x + bounds.width to bounds.y + bounds.height,
            bounds.x to bounds.y + bounds.height
        )
    }

    data object Ellipse : HitShape {
        override fun contains(x: Float, y: Float, bounds: ShapeBounds): Boolean {
            val cx = bounds.x + bounds.width / 2f
            val cy = bounds.y + bounds.height / 2f
            val rx = bounds.width / 2f
            val ry = bounds.height / 2f
            if (rx <= 0f || ry <= 0f) return false
            val nx = (x - cx) / rx
            val ny = (y - cy) / ry
            return (nx * nx + ny * ny) <= 1f
        }

        override fun outlinePoints(bounds: ShapeBounds): List<Pair<Float, Float>> {
            val cx = bounds.x + bounds.width / 2f
            val cy = bounds.y + bounds.height / 2f
            val rx = bounds.width / 2f
            val ry = bounds.height / 2f
            val segments = 24
            return (0 until segments).map { i ->
                val angle = (i.toFloat() / segments) * (Math.PI.toFloat() * 2f)
                (cx + rx * kotlin.math.cos(angle)) to (cy + ry * kotlin.math.sin(angle))
            }
        }
    }

    class Polygon(private val relativePoints: List<Pair<Float, Float>>) : HitShape {
        override fun contains(x: Float, y: Float, bounds: ShapeBounds): Boolean {
            val absPoints = relativePoints.map { (fx, fy) ->
                bounds.x + fx * bounds.width to bounds.y + fy * bounds.height
            }
            return pointInPolygon(x, y, absPoints)
        }

        override fun outlinePoints(bounds: ShapeBounds): List<Pair<Float, Float>> =
            relativePoints.map { (fx, fy) ->
                bounds.x + fx * bounds.width to bounds.y + fy * bounds.height
            }

        private fun pointInPolygon(x: Float, y: Float, points: List<Pair<Float, Float>>): Boolean {
            var inside = false
            var j = points.size - 1
            for (i in points.indices) {
                val (xi, yi) = points[i]
                val (xj, yj) = points[j]
                if ((yi > y) != (yj > y) &&
                    x < (xj - xi) * (y - yi) / (yj - yi) + xi) {
                    inside = !inside
                }
                j = i
            }
            return inside
        }
    }

    class Custom(
        private val predicate: (x: Float, y: Float, bounds: ShapeBounds) -> Boolean,
        private val outlineProvider: ((ShapeBounds) -> List<Pair<Float, Float>>)? = null
    ) : HitShape {
        override fun contains(x: Float, y: Float, bounds: ShapeBounds): Boolean = predicate(x, y, bounds)

        override fun outlinePoints(bounds: ShapeBounds): List<Pair<Float, Float>> =
            outlineProvider?.invoke(bounds) ?: Rectangle.outlinePoints(bounds)
    }
}

data class ShapeBounds(val x: Float, val y: Float, val width: Float, val height: Float)