package computer.obscure.piku.common.scripting.api

import computer.obscure.piku.common.utils.getDecoration
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative

class LuaText : TwineNative("text") {
    @TwineNativeFunction
    fun literal(text: String): LuaTextInstance {
        return LuaTextInstance(text)
    }

    companion object {
        fun fromComponent(component: Component): LuaTextInstance {
            return LuaTextInstance(
                textBold = component.getDecoration(TextDecoration.BOLD),
                textItalic = component.getDecoration(TextDecoration.ITALIC),
                textUnderline = component.getDecoration(TextDecoration.UNDERLINED),
//                textShadowColor = if (component.shadowColor() != null)
//                    LuaColor.fromShadowColor(component.shadowColor()!!) else null
            )
        }
    }
}

class LuaTextInstance(
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
        return Component.text(literalText)
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