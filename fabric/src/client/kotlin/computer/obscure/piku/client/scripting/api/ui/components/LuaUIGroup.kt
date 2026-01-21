package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.piku.client.scripting.api.ui.LuaUI
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.twine.annotations.TwineNativeFunction

class LuaUIGroup(
    override val component: Group
) : AllComponentBuilder(component) {
    @TwineNativeFunction("get")
    fun getById(name: String): LuaUIComponent? {
        return LuaUI.smartGet(component.props.components, name)
    }

    @TwineNativeFunction("exists")
    fun exists(name: String): Boolean {
        return LuaUI.smartGet(component.props.components, name) != null
    }
}