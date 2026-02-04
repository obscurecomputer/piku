package computer.obscure.piku.fabric.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.ui.components.Gradient
import computer.obscure.twine.annotations.TwineNativeFunction

class LuaUIGradient(
    override val component: Gradient
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var from: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.from)
        set(value) {
            component.props.from = value.toUIColor()
        }

    @TwineNativeFunction
    fun from(value: LuaColorInstance): LuaUIGradient {
        component.props.from = value.toUIColor()
        return this
    }

    @TwineNativeProperty
    var to: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.to)
        set(value) {
            component.props.to = value.toUIColor()
        }

    @TwineNativeFunction
    fun to(value: LuaColorInstance): LuaUIGradient {
        component.props.to = value.toUIColor()
        return this
    }

    @TwineNativeProperty
    var fillScreen: Boolean
        get() = component.props.fillScreen
        set(value) {
            component.props.fillScreen = value
        }

    @TwineNativeFunction
    fun fillScreen(value: Boolean = true): LuaUIGradient {
        component.props.fillScreen = value
        return this
    }
}