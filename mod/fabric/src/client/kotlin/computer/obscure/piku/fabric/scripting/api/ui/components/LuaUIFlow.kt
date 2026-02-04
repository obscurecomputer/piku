package computer.obscure.piku.fabric.scripting.api.ui.components

import computer.obscure.piku.fabric.scripting.api.ui.LuaUI
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.ui.classes.FlowDirection
import computer.obscure.piku.common.ui.components.FlowContainer
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty

class LuaUIFlow(
    val flowComponent: FlowContainer
) : AllComponentBuilder(flowComponent) {

    @TwineNativeFunction("get")
    fun getByName(name: String): LuaUIComponent? {
        val found = flowComponent.props.components.find { it.name == name }
        return found?.let { LuaUI.wrap(it) }
    }

    @TwineNativeFunction
    fun exists(name: String): Boolean {
        return flowComponent.props.components.any { it.name == name }
    }

    @TwineNativeProperty
    var backgroundColor: LuaColorInstance?
        get() {
            if (flowComponent.props.backgroundColor == null) return null
            return LuaColor.fromUIColor(flowComponent.props.backgroundColor!!)
        }
        set(value) {
            if (value == null) return
            flowComponent.props.backgroundColor = value.toUIColor()
        }

    @TwineNativeFunction
    fun backgroundColor(value: LuaColorInstance): LuaUIFlow {
        flowComponent.props.backgroundColor = value.toUIColor()
        return this
    }

    @TwineNativeFunction
    fun direction(value: String): LuaUIFlow {
        flowComponent.props.direction = when (value) {
            "h", "horizontal" -> FlowDirection.HORIZONTAL
            "v", "vertical" -> FlowDirection.VERTICAL
            else -> FlowDirection.HORIZONTAL
        }
        return this
    }

    @TwineNativeFunction
    fun gap(value: Double): LuaUIFlow {
        flowComponent.props.gap = value
        return this
    }
}