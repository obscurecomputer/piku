package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.mod.fabric.PikuClient
import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.utils.parseScale
import computer.obscure.piku.mod.fabric.utils.parseScaleRegex
import computer.obscure.twine.annotations.TwineFunction
import net.kyori.adventure.text.Component

open class LuaUIText(override val node: TextNode) : LuaUIFlow(node) {
    private var currentTextInstance = LuaTextInstance("")

    @TwineFunction
    fun text(): LuaTextInstance = currentTextInstance

    @TwineFunction
    fun rawText(): String? = node.rawText

    @TwineFunction
    open fun text(value: String): LuaUIText {
        val component = PikuClient.miniMessage
            .deserialize(value)
        node.originText = component
        node.rawText = value
        currentTextInstance = LuaTextInstance(
            type = "text",
            literalText = value,
            baseComponent = component
        )
        return this
    }

    @TwineFunction
    open fun text(value: LuaTextInstance): LuaUIText {
        node.originText = value.toComponent()
        currentTextInstance = value
        return this
    }

    @TwineFunction
    fun scale(value: Vec2): LuaUIText {
        node.scale = value
        return this
    }

    @TwineFunction
    fun wrap(value: Boolean): LuaUIText {
        node.wrap = value
        return this
    }

    @TwineFunction
    fun setText(value: String): LuaUIText {
        // mutate the MC component's text only
        val current = node.originText
        node.originText = Component.text(value)
            .style(current.style())
        return this
    }

    @TwineFunction
    fun shadow(value: Boolean): LuaUIText {
        node.shadow = value
        return this
    }

    @TwineFunction
    fun scale(value: String): LuaUIText {
        val scale = parseScaleRegex(value)
        node.scaleX = scale.x
        node.scaleY = scale.y
        return this
    }

    @TwineFunction
    fun scale(x: String, y: String): LuaUIText {
        node.scaleX = parseScale(x)
        node.scaleY = parseScale(y)
        return this
    }
}