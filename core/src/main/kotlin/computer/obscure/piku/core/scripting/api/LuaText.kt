package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

class LuaText : TwineNative("text") {
    @TwineFunction
    fun literal(text: String): LuaTextInstance {
        return LuaTextInstance("text", text)
    }

    @TwineFunction
    fun keybind(text: String): LuaTextInstance {
        return LuaTextInstance("keybind", text)
    }
}

class LuaTextInstance(
    var type: String,
    var literalText: String = "",
    var textBold: Boolean = false,
    var textItalic: Boolean = false,
    var textUnderline: Boolean = false,
    var clickEventType: String? = null,
    var clickEvent: String? = null,
    var hoverText: LuaTextInstance? = null,
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

    @TwineFunction
    fun bold(): LuaTextInstance = apply {
        textBold = true
    }

    @TwineFunction
    fun italic(): LuaTextInstance = apply {
        textItalic = true
    }

    @TwineFunction
    fun underline(): LuaTextInstance = apply {
        textUnderline = true
    }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaTextInstance = apply {
        textColor = value
    }

    @TwineFunction
    fun clickEvent(type: String, value: String): LuaTextInstance = apply {
        clickEventType = type
        clickEvent = value
    }

    @TwineFunction
    /**
     * This function can only ever be used in screenshot events.
     */
    fun openFile(): LuaTextInstance = apply {
        clickEventType = "open_file"
        clickEvent = ""
    }

    @TwineFunction
    fun hoverText(value: LuaTextInstance): LuaTextInstance = apply {
        hoverText = value
    }

    @TwineFunction
    fun append(value: LuaTextInstance): LuaTextInstance = apply {
        children.add(value)
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "text[literal=\"$literalText\", bold=$textBold, italic=$textItalic, underline=$textUnderline]"
    }
}

