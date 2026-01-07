package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.ui.components.Box
import computer.obscure.piku.common.ui.components.Gradient

class LuaUIGradient(
    override val component: Gradient
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var from: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.from)
        set(value) {
            component.props.from = value.toUIColor()
        }
    @TwineNativeProperty
    var to: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.to)
        set(value) {
            component.props.to = value.toUIColor()
        }
    @TwineNativeProperty
    var fillScreen: Boolean
        get() = component.props.fillScreen
        set(value) {
            component.props.fillScreen = value
        }
}