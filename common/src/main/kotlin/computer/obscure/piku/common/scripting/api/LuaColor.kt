package computer.obscure.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.common.ui.classes.UIColor

class LuaColor : TwineNative("color") {
    @TwineNativeFunction
    fun rgb(r: Int, g: Int, b: Int): computer.obscure.piku.common.scripting.api.LuaColorInstance {
        return _root_ide_package_.computer.obscure.piku.common.scripting.api.LuaColorInstance(r, g, b)
    }

    companion object {
        fun fromUIColor(uiColor: computer.obscure.piku.common.ui.classes.UIColor): computer.obscure.piku.common.scripting.api.LuaColorInstance {
            return _root_ide_package_.computer.obscure.piku.common.scripting.api.LuaColorInstance(
                uiColor.r, uiColor.g, uiColor.b
            )
        }
    }
}

class LuaColorInstance(
    var r: Int,
    var g: Int,
    var b: Int
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

    fun toUIColor(): UIColor = UIColor(r, g, b)

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "color[r=$r, g=$g, b=$b]"
    }
}
