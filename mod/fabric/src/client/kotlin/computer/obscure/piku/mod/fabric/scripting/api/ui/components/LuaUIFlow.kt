package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUIController
import computer.obscure.piku.mod.fabric.scripting.api.util.Axis
import computer.obscure.piku.mod.fabric.ui.components.FlowNode
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

open class LuaUIFlow(override val node: FlowNode) : LuaUIContainer(node) {

    @TwineFunction
    fun scrollable(value: Boolean): LuaUIFlow {
        node.scrollable = value
        return this
    }

    @TwineFunction
    fun scroll(pixels: Float): LuaUIFlow {
        node.scrollOffset += pixels
        return this
    }

    @TwineFunction
    fun scrollTo(pixels: Float): LuaUIFlow {
        node.scrollOffset = -pixels
        return this
    }

    @TwineFunction
    fun scrollToTop(): LuaUIFlow {
        node.scrollOffset = 0f
        return this
    }

    @TwineFunction
    fun scrollToBottom(): LuaUIFlow {
        node.scrollOffset = -Float.MAX_VALUE
        return this
    }

    @TwineProperty
    val scrollOffset: Float
        get() = -node.clampedScroll

    @TwineProperty
    val maxScroll: Float
        get() = node.maxScrollExtent

    @TwineFunction
    fun gap(value: Double): LuaUIFlow {
        node.gap = value.toFloat()
        return this
    }

    @TwineFunction
    fun mainAxis(value: String): LuaUIFlow {
        node.mainAxis = Axis.parseMainAxis(value)
        return this
    }

    @TwineFunction
    fun crossAxis(value: String): LuaUIFlow {
        node.crossAxis = Axis.parseCrossAxis(value)
        return this
    }

    @TwineFunction
    fun controller(): LuaUIController {
        return LuaUIController(node)
    }
}