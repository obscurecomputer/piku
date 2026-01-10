package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.ui.classes.UIColor
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty

class LuaUIProgressBar (
    override val component: ProgressBar
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var progress: Float
        get() = component.props.progress
        set(value) {
            component.props.progress = value
        }

    @TwineNativeFunction
    fun progress(value: Float): LuaUIProgressBar {
        component.props.progress = value
        return this
    }

    @TwineNativeProperty
    var fillColor: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.fillColor ?: UIColor.BLACK)
        set(value) {
            component.props.fillColor = value.toUIColor()
        }

    @TwineNativeFunction
    fun fillColor(value: LuaColorInstance): LuaUIProgressBar {
        component.props.fillColor = value.toUIColor()
        return this
    }

    @TwineNativeProperty
    var emptyColor: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.emptyColor ?: UIColor.BLACK)
        set(value) {
            component.props.emptyColor = value.toUIColor()
        }

    @TwineNativeFunction
    fun emptyColor(value: LuaColorInstance): LuaUIProgressBar {
        component.props.emptyColor = value.toUIColor()
        return this
    }
}