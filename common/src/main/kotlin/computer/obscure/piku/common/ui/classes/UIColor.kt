package computer.obscure.piku.common.ui.classes

data class UIColor(
    val r: Int,
    val g: Int,
    val b: Int,
    var a: Int = 255
) {
    fun toArgb(): Int =
        (a shl 24) or (r shl 16) or (g shl 8) or b

    companion object {
        val BLACK = UIColor(0, 0, 0, 255)
        val WHITE = UIColor(255, 255, 255, 255)
    }
}