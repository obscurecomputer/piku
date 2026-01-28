package computer.obscure.piku.common.classes

data class Vec2(
    val x: Double = 0.0,
    val y: Double = 0.0
) {
    fun add(vec2: Vec2): Vec2 {
        return Vec2(x + vec2.x, y + vec2.y)
    }
    fun add(x: Double, y: Double): Vec2 {
        return Vec2(this.x + x, this.y + y)
    }

    fun lineTo(to: Vec2): List<Vec2> {
        val points = mutableListOf<Vec2>()

        var x1 = x.toInt()
        var y1 = y.toInt()
        val x2 = to.x.toInt()
        val y2 = to.y.toInt()

        // absolute distance between from and to
        val dx = kotlin.math.abs(x2 - x1)
        val dy = kotlin.math.abs(y2 - y1)

        // step direction
        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1
        var err = dx - dy

        // todo: find out if this is dangerous
        while (true) {
            points.add(Vec2(x1.toDouble(), y1.toDouble()))
            if (x1 == x2 && y1 == y2) break
            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x1 += sx
            }
            if (e2 < dx) {
                err += dx
                y1 += sy
            }
        }

        return points
    }
}