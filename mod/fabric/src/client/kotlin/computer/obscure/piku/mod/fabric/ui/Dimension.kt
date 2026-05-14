package computer.obscure.piku.mod.fabric.ui

sealed class Dimension {
    object Wrap : Dimension()
    object Fill : Dimension()
    data class Fixed(val px: Float) : Dimension()
    data class Fraction(val frac: Float) : Dimension()
}