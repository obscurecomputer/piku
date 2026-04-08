package computer.obscure.piku.core.classes

data class Vec3(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
) {
    constructor(x: Number, y: Number, z: Number) : this(
        x.toDouble(),
        y.toDouble(),
        z.toDouble()
    )

    companion object {
        val ZERO = Vec3(0.0, 0.0, 0.0)
    }
}