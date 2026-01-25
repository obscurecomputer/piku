package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.piku.client.scripting.api.ui.LuaUI
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.twine.annotations.TwineNativeFunction

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
}