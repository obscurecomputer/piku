package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Group
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUI
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaUIGroup(
    override val component: Group
) : AllComponentBuilder(component) {
    @TwineFunction("get")
    fun getByName(name: String): LuaUIComponent? {
        val found = component.props.components.find { it.name == name }
        return found?.let { LuaUI.wrap(it) }
    }

    @TwineFunction
    fun exists(name: String): Boolean {
        return component.props.components.any { it.name == name }
    }

    @TwineProperty
    var backgroundColor: LuaColorInstance?
        get() {
            if (component.props.backgroundColor == null) return null
            return LuaColor.fromUIColor(component.props.backgroundColor!!)
        }
        set(value) {
            if (value == null) return
            component.props.backgroundColor = value.toUIColor()
        }

    @TwineFunction
    fun backgroundColor(value: LuaColorInstance): LuaUIComponent {
        component.props.backgroundColor = value.toUIColor()
        return this
    }
}