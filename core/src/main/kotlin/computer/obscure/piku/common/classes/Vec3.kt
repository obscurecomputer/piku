package computer.obscure.piku.common.classes

data class Vec3(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
) {
    constructor(x: Float, y: Float, z: Float) : this(
        x.toDouble(),
        y.toDouble(),
        z.toDouble()
    )
}