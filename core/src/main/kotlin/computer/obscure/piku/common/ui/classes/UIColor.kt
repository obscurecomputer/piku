package computer.obscure.piku.common.ui.classes

import net.kyori.adventure.text.format.TextColor

data class UIColor(
    val r: Int,
    val g: Int,
    val b: Int,
    var a: Int = 255
) {
    fun toArgb(): Int =
        (a shl 24) or (r shl 16) or (g shl 8) or b

    fun toTextColor(): TextColor = TextColor.color(r, g, b)

    companion object {
        val BLACK = UIColor(0, 0, 0, 255)
        val WHITE = UIColor(255, 255, 255, 255)
    }
}