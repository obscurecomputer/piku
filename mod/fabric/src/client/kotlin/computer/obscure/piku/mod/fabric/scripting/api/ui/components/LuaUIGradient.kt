package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Gradient
import computer.obscure.twine.annotations.TwineFunction

class LuaUIGradient(
    override val component: Gradient
) : LuaUIComponent(component) {
    @TwineProperty
    var from: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.from)
        set(value) {
            component.props.from = value.toUIColor()
        }

    @TwineFunction
    fun from(value: LuaColorInstance): LuaUIGradient {
        component.props.from = value.toUIColor()
        return this
    }

    @TwineProperty
    var to: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.to)
        set(value) {
            component.props.to = value.toUIColor()
        }

    @TwineFunction
    fun to(value: LuaColorInstance): LuaUIGradient {
        component.props.to = value.toUIColor()
        return this
    }

    @TwineProperty
    var fillScreen: Boolean
        get() = component.props.fillScreen
        set(value) {
            component.props.fillScreen = value
        }

    @TwineFunction
    fun fillScreen(value: Boolean = true): LuaUIGradient {
        component.props.fillScreen = value
        return this
    }
}