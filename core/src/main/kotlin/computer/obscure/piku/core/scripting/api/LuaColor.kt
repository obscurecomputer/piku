package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import computer.obscure.piku.core.ui.classes.UIColor
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

class LuaColor : TwineNative("color") {
    @TwineFunction
    fun rgb(r: Int, g: Int, b: Int): LuaColorInstance {
        return LuaColorInstance(r, g, b)
    }

    @TwineFunction
    fun rgba(r: Int, g: Int, b: Int, a: Int): LuaColorInstance {
        return LuaColorInstance(r, g, b, a)
    }

    companion object {
        fun fromUIColor(uiColor: UIColor): LuaColorInstance {
            return LuaColorInstance(
                uiColor.r, uiColor.g, uiColor.b, uiColor.a
            )
        }

        fun fromShadowColor(shadowColor: ShadowColor): LuaColorInstance {
            return LuaColorInstance(
                shadowColor.red(),
                shadowColor.green(),
                shadowColor.blue(),
                shadowColor.alpha()
            )
        }
    }
}

class LuaColorInstance(
    @TwineProperty
    var r: Int,
    @TwineProperty
    var g: Int,
    @TwineProperty
    var b: Int,
    @TwineProperty
    var a: Int = 255
) : TwineNative() {

    fun toUIColor(): UIColor = UIColor(r, g, b, a)

    fun toShadowColor(): ShadowColor = ShadowColor.shadowColor(r, g, b, a)
    fun toTextColor(): TextColor = TextColor.color(r, g, b)

    @TwineFunction("tostring")
    override fun toString(): String {
        return "color[r=$r, g=$g, b=$b, a=$a]"
    }
}
