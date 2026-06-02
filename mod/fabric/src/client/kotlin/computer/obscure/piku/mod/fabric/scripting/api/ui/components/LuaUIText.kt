package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.utils.toMcComponent
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.network.chat.Component

class LuaUIText(override val node: TextNode) : LuaUIContainer(node) {
    private var currentTextInstance: LuaTextInstance = LuaTextInstance("")

    @TwineFunction
    fun text(): LuaTextInstance = currentTextInstance

    @TwineFunction
    fun text(value: String): LuaUIText {
        node.text = Component.literal(value)
        currentTextInstance = LuaTextInstance(value)
        return this
    }

    @TwineFunction
    fun text(value: LuaTextInstance): LuaUIText {
        node.text = value.toMcComponent()
        currentTextInstance = value
        return this
    }

    @TwineFunction
    fun scale(value: LuaVec2Instance): LuaUIText {
        node.scale = value.toVec2()
        return this
    }

    @TwineFunction
    fun setText(value: String): LuaUIText {
        // mutate the MC component's text only
        val current = node.text
        node.text = Component.literal(value)
            .withStyle(current.style)
        return this
    }

    @TwineFunction
    fun shadow(value: Boolean): LuaUIText {
        node.shadow = value
        return this
    }

    private fun parseScale(value: String): ScaleDimension = when {
        value.endsWith("pw") -> ScaleDimension.ParentWidth(value.dropLast(2).toDouble() / 100.0)
        value.endsWith("ph") -> ScaleDimension.ParentHeight(value.dropLast(2).toDouble() / 100.0)
        value.endsWith("%") -> ScaleDimension.Fixed(value.dropLast(1).toDouble() / 100.0)
        value.endsWith("x") -> ScaleDimension.Fixed(value.dropLast(1).toDouble())
        else -> ScaleDimension.Fixed(value.toDouble())
    }

    @TwineFunction
    fun scale(value: String): LuaUIText {
        val parts = value.trim().split("\\s+".toRegex())
        if (parts.size == 2) {
            node.scaleX = parseScale(parts[0])
            node.scaleY = parseScale(parts[1])
        } else {
            val s = parseScale(value)
            node.scaleX = s
            node.scaleY = s
        }
        return this
    }

    @TwineFunction
    fun scale(x: String, y: String): LuaUIText {
        node.scaleX = parseScale(x)
        node.scaleY = parseScale(y)
        return this
    }
}