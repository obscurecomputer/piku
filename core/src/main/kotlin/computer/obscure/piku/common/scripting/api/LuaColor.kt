package computer.obscure.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.common.ui.classes.UIColor
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

class LuaColor : TwineNative("color") {
    @TwineNativeFunction
    fun rgb(r: Int, g: Int, b: Int): LuaColorInstance {
        return LuaColorInstance(r, g, b)
    }

    @TwineNativeFunction
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
    @TwineNativeProperty
    var r: Int,
    @TwineNativeProperty
    var g: Int,
    @TwineNativeProperty
    var b: Int,
    @TwineNativeProperty
    var a: Int = 255
) : TwineNative() {

    fun toUIColor(): UIColor = UIColor(r, g, b, a)

    fun toShadowColor(): ShadowColor = ShadowColor.shadowColor(r, g, b, a)
    fun toTextColor(): TextColor = TextColor.color(r, g, b)

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "color[r=$r, g=$g, b=$b, a=$a]"
    }
}
