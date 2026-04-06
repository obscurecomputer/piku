package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.classes.FillDirection
import computer.obscure.piku.core.ui.classes.UIColor
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaUIProgressBar (
    override val component: ProgressBar
) : LuaUIComponent(component) {
    @TwineProperty
    var progress: Float
        get() = component.props.progress
        set(value) {
            component.props.progress = value
        }

    @TwineFunction
    fun progress(value: Float): LuaUIProgressBar {
        component.props.progress = value
        return this
    }

    @TwineProperty
    var fillColor: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.fillColor ?: UIColor.BLACK)
        set(value) {
            component.props.fillColor = value.toUIColor()
        }

    @TwineFunction
    fun fillColor(value: LuaColorInstance): LuaUIProgressBar {
        component.props.fillColor = value.toUIColor()
        return this
    }

    @TwineProperty
    var emptyColor: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.emptyColor ?: UIColor.BLACK)
        set(value) {
            component.props.emptyColor = value.toUIColor()
        }

    @TwineFunction
    fun emptyColor(value: LuaColorInstance): LuaUIProgressBar {
        component.props.emptyColor = value.toUIColor()
        return this
    }

    @TwineFunction
    fun fillDirection(value: String): LuaUIProgressBar {
        component.props.fillDirection = when (value) {
            "top" -> FillDirection.TOP
            "left" -> FillDirection.LEFT
            "bottom" -> FillDirection.BOTTOM
            "right" -> FillDirection.RIGHT

            "down" -> FillDirection.TOP
            "up" -> FillDirection.BOTTOM
            else -> FillDirection.RIGHT
        }
        return this
    }
}