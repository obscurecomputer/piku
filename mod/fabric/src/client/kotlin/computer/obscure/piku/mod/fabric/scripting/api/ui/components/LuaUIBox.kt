package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Box
import computer.obscure.twine.annotations.TwineFunction

class LuaUIBox(
    override val component: Box
) : LuaUIComponent(component) {
    @TwineFunction
    fun color(): LuaColorInstance = LuaColor.fromUIColor(component.props.color)

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUIComponent {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineFunction
    fun fillScreen(): LuaUIComponent {
        component.props.fillScreen = true
        return this
    }

    @TwineFunction
    fun fillScreen(value: Boolean): LuaUIComponent {
        component.props.fillScreen = value
        return this
    }
}