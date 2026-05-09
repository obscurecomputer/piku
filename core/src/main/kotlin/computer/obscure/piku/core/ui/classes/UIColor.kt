package computer.obscure.piku.core.ui.classes

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.serialization.PikuSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.TextColor

@Serializable
@SerialName("color")
data class UIColor(
    val r: Int,
    val g: Int,
    val b: Int,
    var a: Int = 255
): PikuSerializable() {
    override val typeName = "color"
    override fun toLuaInstance() = LuaColor.fromUIColor(this)

    fun toArgb(): Int =
        (a shl 24) or (r shl 16) or (g shl 8) or b
    fun toTextColor(): TextColor = TextColor.color(r, g, b)

    companion object {
        val BLACK = UIColor(0, 0, 0, 255)
        val WHITE = UIColor(255, 255, 255, 255)
    }
}