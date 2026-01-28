package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.piku.client.scripting.api.ui.LuaUI
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty

class LuaUIGroup(
    override val component: Group
) : AllComponentBuilder(component) {
    @TwineNativeFunction("get")
    fun getByName(name: String): LuaUIComponent? {
        val found = component.props.components.find { it.name == name }
        return found?.let { LuaUI.wrap(it) }
    }

    @TwineNativeFunction
    fun exists(name: String): Boolean {
        return component.props.components.any { it.name == name }
    }

    @TwineNativeProperty
    var backgroundColor: LuaColorInstance?
        get() {
            if (component.props.backgroundColor == null) return null
            return LuaColor.fromUIColor(component.props.backgroundColor!!)
        }
        set(value) {
            if (value == null) return
            component.props.backgroundColor = value.toUIColor()
        }

    @TwineNativeFunction
    fun backgroundColor(value: LuaColorInstance): LuaUIComponent {
        component.props.backgroundColor = value.toUIColor()
        return this
    }
}