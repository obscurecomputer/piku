package computer.obscure.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

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
    @TwineNativeProperty
    var clickEventType: String? = null,
    @TwineNativeProperty
    var clickEvent: String? = null,

    @TwineNativeProperty
    var hoverText: LuaTextInstance? = null,

    @TwineNativeProperty
    var textColor: LuaColorInstance? = null
) : TwineNative() {

    val children: MutableList<LuaTextInstance> = mutableListOf()

    fun toComponent(): Component {
        var component: Component = when(type) {
            "text" -> Component.text(literalText)
            "keybind" -> Component.keybind(literalText)
            "translatable" -> Component.translatable(literalText)
            else -> Component.text(literalText)
        }

        when (clickEventType?.lowercase()) {
            "open_file" -> {
                // OPEN_FILE is supported, but this is handled on the modded client since
                // Adventure does not support it very well
            }
        }

        if (hoverText != null) {
            component = component.hoverEvent(
                HoverEvent.showText(
                    hoverText!!.toComponent()
                )
            )
        }

        return component
            .decoration(TextDecoration.BOLD, textBold)
            .decoration(TextDecoration.ITALIC, textItalic)
            .decoration(TextDecoration.UNDERLINED, textUnderline)
            .color(textColor?.toTextColor())
            .append(children.map { it.toComponent() })
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

    @TwineNativeFunction
    fun color(value: LuaColorInstance): LuaTextInstance = apply {
        textColor = value
    }

    @TwineNativeFunction
    fun clickEvent(type: String, value: String): LuaTextInstance = apply {
        clickEventType = type
        clickEvent = value
    }

    @TwineNativeFunction
    /**
     * This function can only ever be used in screenshot events.
     */
    fun openFile(): LuaTextInstance = apply {
        clickEventType = "open_file"
        clickEvent = ""
    }

    @TwineNativeFunction
    fun hoverText(value: LuaTextInstance): LuaTextInstance = apply {
        hoverText = value
    }

    @TwineNativeFunction
    fun append(value: LuaTextInstance): LuaTextInstance = apply {
        children.add(value)
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "text[literal=\"$literalText\", bold=$textBold, italic=$textItalic, underline=$textUnderline]"
    }
}

