package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Gradient
import computer.obscure.twine.annotations.TwineFunction

class LuaUIGradient(
    override val component: Gradient
) : LuaUIComponent(component) {
    @TwineFunction
    fun from(): LuaColorInstance = LuaColor.fromUIColor(component.props.from)

    @TwineFunction
    fun from(value: LuaColorInstance): LuaUIGradient {
        component.props.from = value.toUIColor()
        return this
    }

    @TwineFunction
    fun to(): LuaColorInstance = LuaColor.fromUIColor(component.props.to)

    @TwineFunction
    fun to(value: LuaColorInstance): LuaUIGradient {
        component.props.to = value.toUIColor()
        return this
    }

    @TwineFunction
    fun fillScreen(): Boolean = component.props.fillScreen

    @TwineFunction
    fun fillScreen(value: Boolean = true): LuaUIGradient {
        component.props.fillScreen = value
        return this
    }
}