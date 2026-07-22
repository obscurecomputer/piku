package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import me.znotchill.kiwi.generated.Color
import me.znotchill.kiwi.generated.toTextColor
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.ShadowColor
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

    @TwineFunction
    fun translatable(key: String): LuaTextInstance {
        return LuaTextInstance("translatable", key)
    }

    companion object {
        fun fromComponent(component: Component): LuaTextInstance {
            return LuaTextInstance(
                baseComponent = component,
                type = "text"
            )
        }
    }
}

class LuaTextInstance(
    var type: String,
    @TwineProperty
    var literalText: String = "",

    var textBold: Boolean = false,
    var textItalic: Boolean = false,
    var textUnderline: Boolean = false,
    var textStrikethrough: Boolean = false,
    var textObfuscated: Boolean = false,

    var insertionText: String? = null,
    var font: String? = null,

    var clickEventType: String? = null,
    var clickEvent: String? = null,

    var hoverText: LuaTextInstance? = null,

    var textColor: Color? = null,
    var textShadowColor: Color? = null,

    private val baseComponent: Component? = null,
) : TwineNative() {

    val children: MutableList<LuaTextInstance> = mutableListOf()

    fun toComponent(): Component {
        var component = baseComponent ?: when (type) {
            "text" -> Component.text(literalText)
            "keybind" -> Component.keybind(literalText)
            "translatable" -> Component.translatable(literalText)
            else -> Component.text(literalText)
        }

        when (clickEventType?.lowercase()) {
            "open_url" -> component = component.clickEvent(
                ClickEvent.openUrl(clickEvent!!)
            )

            "run_command" -> component = component.clickEvent(
                ClickEvent.runCommand(clickEvent!!)
            )

            "suggest_command" -> component = component.clickEvent(
                ClickEvent.suggestCommand(clickEvent!!)
            )

            "copy_to_clipboard" -> component = component.clickEvent(
                ClickEvent.copyToClipboard(clickEvent!!)
            )

            "change_page" -> component = component.clickEvent(
                ClickEvent.changePage(clickEvent!!.toInt())
            )

            "open_file" -> {
                // handled client-side
            }
        }

        if (hoverText != null) {
            component = component.hoverEvent(
                HoverEvent.showText(
                    hoverText!!.toComponent()
                )
            )
        }

        if (insertionText != null) {
            component = component.insertion(insertionText!!)
        }

        if (font != null) {
            component = component.font(Key.key(font!!))
        }

        for (child in children) {
            component = component.append(child.toComponent())
        }

        return component
            .decoration(TextDecoration.BOLD, textBold)
            .decoration(TextDecoration.ITALIC, textItalic)
            .decoration(TextDecoration.UNDERLINED, textUnderline)
            .decoration(TextDecoration.STRIKETHROUGH, textStrikethrough)
            .decoration(TextDecoration.OBFUSCATED, textObfuscated)
            .color(textColor?.toTextColor())
            .shadowColor(
                textShadowColor?.let {
                    ShadowColor.shadowColor(it.r, it.g, it.b, it.a)
                }
            )
    }

    @TwineFunction
    fun text(value: String): LuaTextInstance = apply {
        literalText = value
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
    fun strikethrough(): LuaTextInstance = apply {
        textStrikethrough = true
    }

    @TwineFunction
    fun obfuscated(): LuaTextInstance = apply {
        textObfuscated = true
    }

    @TwineFunction
    fun color(value: Color): LuaTextInstance = apply {
        textColor = value
    }

    @TwineFunction
    fun shadowColor(value: Color): LuaTextInstance = apply {
        textShadowColor = value
    }

    @TwineFunction
    fun insertion(value: String): LuaTextInstance = apply {
        insertionText = value
    }

    @TwineFunction
    fun font(value: String): LuaTextInstance = apply {
        font = value
    }

    @TwineFunction
    fun clickEvent(type: String, value: String): LuaTextInstance = apply {
        clickEventType = type
        clickEvent = value
    }

    @TwineFunction
    fun openUrl(url: String): LuaTextInstance = apply {
        clickEventType = "open_url"
        clickEvent = url
    }

    @TwineFunction
    fun runCommand(command: String): LuaTextInstance = apply {
        clickEventType = "run_command"
        clickEvent = command
    }

    @TwineFunction
    fun suggestCommand(command: String): LuaTextInstance = apply {
        clickEventType = "suggest_command"
        clickEvent = command
    }

    @TwineFunction
    fun copyToClipboard(text: String): LuaTextInstance = apply {
        clickEventType = "copy_to_clipboard"
        clickEvent = text
    }

    @TwineFunction
    fun changePage(page: Int): LuaTextInstance = apply {
        clickEventType = "change_page"
        clickEvent = page.toString()
    }

    /**
     * This function can only ever be used in screenshot events.
     */
    @TwineFunction
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
        return buildString {
            append("text[")
            append("literal=\"$literalText\"")
            append(", bold=$textBold")
            append(", italic=$textItalic")
            append(", underline=$textUnderline")
            append(", strikethrough=$textStrikethrough")
            append(", obfuscated=$textObfuscated")
            append("]")
        }
    }
}