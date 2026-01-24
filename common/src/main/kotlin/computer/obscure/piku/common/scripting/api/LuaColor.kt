package computer.obscure.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.common.ui.classes.UIColor
import net.kyori.adventure.text.format.ShadowColor

class LuaColor : TwineNative("color") {
    @TwineNativeFunction
    fun rgb(r: Int, g: Int, b: Int): LuaColorInstance {
        return LuaColorInstance(r, g, b)
    }

    companion object {
        fun fromUIColor(uiColor: UIColor): LuaColorInstance {
            return LuaColorInstance(
                uiColor.r, uiColor.g, uiColor.b
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
    var r: Int,
    var g: Int,
    var b: Int,
    var a: Int = 0
) : TwineNative() {
    @TwineNativeProperty("r")
    var red: Int
        get() = r
        set(value) { r = value }

    @TwineNativeProperty("g")
    var green: Int
        get() = g
        set(value) { g = value }

    @TwineNativeProperty("b")
    var blue: Int
        get() = b
        set(value) { b = value }

    @TwineNativeProperty("a")
    var alpha: Int
        get() = a
        set(value) { a = value }

    fun toUIColor(): UIColor = UIColor(r, g, b, a)

    fun toShadowColor(): ShadowColor = ShadowColor.shadowColor(r, g, b, a)

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "color[r=$r, g=$g, b=$b, a=$a]"
    }
}
