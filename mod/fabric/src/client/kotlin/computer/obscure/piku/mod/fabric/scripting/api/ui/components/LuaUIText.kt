package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaTextInstance
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
    fun setText(value: String): LuaUIText {
        // mutate the MC component's text only
        val current = node.text
        node.text = Component.literal(value)
            .withStyle(current.style)
        return this
    }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUIText {
        node.color = value.toUIColor()
        return this
    }

    @TwineFunction
    fun shadow(value: Boolean): LuaUIText {
        node.shadow = value
        return this
    }
}