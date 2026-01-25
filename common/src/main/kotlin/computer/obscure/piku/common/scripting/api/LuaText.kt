package computer.obscure.piku.common.scripting.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative

class LuaText : TwineNative("text") {
    @TwineNativeFunction
    fun literal(text: String): LuaTextInstance {
        return LuaTextInstance("text", text)
    }

    @TwineNativeFunction
    fun keybind(text: String): LuaTextInstance {
        return LuaTextInstance("keybind", text)
    }
}

class LuaTextInstance(
    @TwineNativeProperty
    var type: String,
    @TwineNativeProperty
    var literalText: String = "",
    @TwineNativeProperty
    var textBold: Boolean = false,
    @TwineNativeProperty
    var textItalic: Boolean = false,
    @TwineNativeProperty
    var textUnderline: Boolean = false,
) : TwineNative() {
    fun toComponent(): Component {
        val component = when(type) {
            "text" -> Component.text(literalText)
            "keybind" -> Component.keybind(literalText)
            "translatable" -> Component.translatable(literalText)
            else -> Component.text(literalText)
        }
        return component
            .decoration(TextDecoration.BOLD, textBold)
            .decoration(TextDecoration.ITALIC, textItalic)
            .decoration(TextDecoration.UNDERLINED, textUnderline)
//            .shadowColor(textShadowColor?.toShadowColor())
    }

    @TwineNativeFunction
    fun bold(): LuaTextInstance = apply {
        textBold = true
    }

    @TwineNativeFunction
    fun italic(): LuaTextInstance = apply {
        textItalic = true
    }

    @TwineNativeFunction
    fun underline(): LuaTextInstance = apply {
        textUnderline = true
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "text[literal=\"$literalText\", bold=$textBold, italic=$textItalic, underline=$textUnderline]"
    }
}

