package computer.obscure.piku.mod.fabric.ui.classes

sealed class ScaleDimension {
    data object One : ScaleDimension()
    data class Fixed(val value: Double) : ScaleDimension()
    data class Fraction(val frac: Double) : ScaleDimension()
    data class ParentWidth(val frac: Double) : ScaleDimension()
    data class ParentHeight(val frac: Double) : ScaleDimension()

    fun resolve(parentScale: Double, parentWidth: Float, parentHeight: Float, baseTextSize: Float): Double = when (this) {
        is One -> 1.0
        is Fixed -> value
        is Fraction -> parentScale * frac
        is ParentWidth -> (parentWidth * frac) / baseTextSize
        is ParentHeight -> (parentHeight * frac) / baseTextSize
    }
}