package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.ui.components.Box
import computer.obscure.twine.annotations.TwineNativeFunction

class LuaUIBox(
    override val component: Box
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }

    @TwineNativeFunction
    fun color(value: LuaColorInstance): LuaUIComponent {
        component.props.color = value.toUIColor()
        return this
    }
}