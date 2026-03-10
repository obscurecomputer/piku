package computer.obscure.piku.mod.common.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Box
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

    @TwineNativeProperty
    var fillScreen: Boolean
        get() = component.props.fillScreen
        set(value) {
            component.props.fillScreen = value
        }

    @TwineNativeFunction
    fun fillScreen(): LuaUIComponent {
        component.props.fillScreen = true
        return this
    }
}