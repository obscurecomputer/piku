package computer.obscure.piku.mod.fabric.ui.classes

sealed class OffsetDimension {
    data object Zero : OffsetDimension()
    data class Fixed(val px: Float) : OffsetDimension()
    data class Fraction(val frac: Float) : OffsetDimension()

    fun resolve(parentSize: Float): Float = when (this) {
        is Zero -> 0f
        is Fixed -> px
        is Fraction -> parentSize * frac
    }
}