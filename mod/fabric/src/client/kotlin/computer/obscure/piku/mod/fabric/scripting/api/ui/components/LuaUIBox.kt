package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Box
import computer.obscure.twine.annotations.TwineFunction

class LuaUIBox(
    override val component: Box
) : LuaUIComponent(component) {
    @TwineProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUIComponent {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineProperty
    var fillScreen: Boolean
        get() = component.props.fillScreen
        set(value) {
            component.props.fillScreen = value
        }

    @TwineFunction
    fun fillScreen(): LuaUIComponent {
        component.props.fillScreen = true
        return this
    }
}