package computer.obscure.piku.core.classes

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.generated.Vec3
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

@Serializable
data class BlockVec(
    @TwineProperty val x: Int,
    @TwineProperty val y: Int,
    @TwineProperty val z: Int,
) : TwineNative("blockvec") {

    @TwineFunction
    fun add(offset: BlockVec): BlockVec = copy(x = x + offset.x, y = y + offset.y, z = z + offset.z)

    @TwineFunction
    fun add(x: Int, y: Int, z: Int): BlockVec = copy(x = this.x + x, y = this.y + y, z = this.z + z)

    @TwineFunction
    fun sub(other: BlockVec): BlockVec = copy(x = x - other.x, y = y - other.y, z = z - other.z)

    @TwineFunction
    fun sub(x: Int, y: Int, z: Int): BlockVec = copy(x = this.x - x, y = this.y - y, z = this.z - z)

    @TwineFunction
    fun scale(factor: Int): BlockVec = copy(x = x * factor, y = y * factor, z = z * factor)

    @TwineFunction
    fun multiply(other: BlockVec): BlockVec = copy(x = x * other.x, y = y * other.y, z = z * other.z)

    @TwineFunction
    fun floorDiv(divisor: Int): BlockVec = copy(
        x = Math.floorDiv(x, divisor),
        y = Math.floorDiv(y, divisor),
        z = Math.floorDiv(z, divisor),
    )

    @TwineFunction
    fun negate(): BlockVec = copy(x = -x, y = -y, z = -z)

    @TwineFunction
    fun abs(): BlockVec = copy(x = abs(x), y = abs(y), z = abs(z))

    @TwineFunction fun up(): BlockVec = copy(y = y + 1)
    @TwineFunction fun up(n: Int): BlockVec = copy(y = y + n)
    @TwineFunction fun down(): BlockVec = copy(y = y - 1)
    @TwineFunction fun down(n: Int): BlockVec = copy(y = y - n)
    @TwineFunction fun north(): BlockVec = copy(z = z - 1)
    @TwineFunction fun north(n: Int): BlockVec = copy(z = z - n)
    @TwineFunction fun south(): BlockVec = copy(z = z + 1)
    @TwineFunction fun south(n: Int): BlockVec = copy(z = z + n)
    @TwineFunction fun east(): BlockVec = copy(x = x + 1)
    @TwineFunction fun east(n: Int): BlockVec = copy(x = x + n)
    @TwineFunction fun west(): BlockVec = copy(x = x - 1)
    @TwineFunction fun west(n: Int): BlockVec = copy(x = x - n)

    @TwineFunction
    fun min(other: BlockVec): BlockVec = copy(x = min(x, other.x), y = min(y, other.y), z = min(z, other.z))

    @TwineFunction
    fun max(other: BlockVec): BlockVec = copy(x = max(x, other.x), y = max(y, other.y), z = max(z, other.z))

    @TwineFunction
    fun clamp(lo: BlockVec, hi: BlockVec): BlockVec = copy(
        x = x.coerceIn(lo.x, hi.x),
        y = y.coerceIn(lo.y, hi.y),
        z = z.coerceIn(lo.z, hi.z),
    )

    @TwineFunction
    fun isZero(): Boolean = x == 0 && y == 0 && z == 0

    @TwineFunction
    fun equals(x: Int, y: Int, z: Int): Boolean =
        this.x == x && this.y == y && this.z == z

    @TwineFunction
    fun isWithin(a: BlockVec, b: BlockVec): Boolean =
        x in min(a.x, b.x)..max(a.x, b.x) &&
                y in min(a.y, b.y)..max(a.y, b.y) &&
                z in min(a.z, b.z)..max(a.z, b.z)

    @TwineFunction
    fun dot(other: BlockVec): Double =
        x.toDouble() * other.x + y.toDouble() * other.y + z.toDouble() * other.z

    @TwineFunction
    fun cross(other: BlockVec): BlockVec = BlockVec(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x,
    )

    @TwineFunction
    fun lengthSquared(): Double = dot(this)

    @TwineFunction
    fun length(): Double = sqrt(lengthSquared())

    @TwineFunction
    fun distanceSquared(other: BlockVec): Double = sub(other).lengthSquared()

    @TwineFunction
    fun distance(other: BlockVec): Double = sqrt(distanceSquared(other))

    @TwineFunction
    fun manhattan(other: BlockVec): Int =
        abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    @TwineFunction
    fun chebyshev(other: BlockVec): Int =
        max(abs(x - other.x), max(abs(y - other.y), abs(z - other.z)))

    @TwineFunction
    fun rotateY(quarterTurns: Int): BlockVec = when (Math.floorMod(quarterTurns, 4)) {
        1 -> copy(x = -z, z = x)
        2 -> copy(x = -x, z = -z)
        3 -> copy(x = z, z = -x)
        else -> copy()
    }

    @TwineFunction fun chunkX(): Int = x shr 4
    @TwineFunction fun chunkZ(): Int = z shr 4
    @TwineFunction fun sectionY(): Int = y shr 4

    @TwineFunction
    fun chunkLocal(): BlockVec = BlockVec(
        x and 15,
        y and 15,
        z and 15
    )

    @TwineFunction
    fun sectionOrigin(): BlockVec = BlockVec(
        x and 15.inv(),
        y and 15.inv(),
        z and 15.inv()
    )

    @TwineFunction
    fun toVec3(): Vec3 = Vec3(x.toDouble(), y.toDouble(), z.toDouble())

    @TwineFunction
    fun center(): Vec3 = Vec3(x + 0.5, y + 0.5, z + 0.5)

    @TwineFunction
    fun copyOf(): BlockVec = copy()

    operator fun plus(o: BlockVec) = add(o)
    operator fun minus(o: BlockVec) = sub(o)
    operator fun times(n: Int) = scale(n)
    operator fun unaryMinus() = negate()

    companion object {
        val ZERO = BlockVec(0, 0, 0)
    }
}