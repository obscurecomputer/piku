package computer.obscure.piku.mod.fabric.utils

import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.OffsetDimension
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension2

fun parseDimension(value: String): Dimension = when {
    value == "wrap" -> Dimension.Wrap
    value == "fill" -> Dimension.Fill
    value.endsWith("%") -> Dimension.Fraction(value.dropLast(1).toFloat() / 100f)
    value.endsWith("px") -> Dimension.Fixed(value.dropLast(2).toFloat())
    else -> Dimension.Fixed(value.toFloat())
}

fun parseOffsetDimension(value: String): OffsetDimension = when {
    value.endsWith("%") -> OffsetDimension.Fraction(value.dropLast(1).toFloat() / 100f)
    value.endsWith("px") -> OffsetDimension.Fixed(value.dropLast(2).toFloat())
    else -> OffsetDimension.Fixed(value.toFloat())
}

fun parseScale(value: String): ScaleDimension = when {
    value.endsWith("pw") -> ScaleDimension.ParentWidth(value.dropLast(2).toDouble() / 100.0)
    value.endsWith("ph") -> ScaleDimension.ParentHeight(value.dropLast(2).toDouble() / 100.0)
    value.endsWith("%") -> ScaleDimension.Fixed(value.dropLast(1).toDouble() / 100.0)
    value.endsWith("x") -> ScaleDimension.Fixed(value.dropLast(1).toDouble())
    else -> ScaleDimension.Fixed(value.toDouble())
}

fun parseScaleRegex(value: String): ScaleDimension2 {
    val parts = value.trim().split("\\s+".toRegex())
    if (parts.size == 2) {
        return ScaleDimension2(
            parseScale(parts[0]),
            parseScale(parts[1])
        )
    } else {
        val s = parseScale(value)
        return ScaleDimension2(s, s)
    }
}