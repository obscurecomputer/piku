package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.classes.FillDirection
import computer.obscure.piku.core.ui.classes.UIColor
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.twine.annotations.TwineFunction

class LuaUIProgressBar (
    override val component: ProgressBar
) : LuaUIComponent(component) {
    @TwineFunction
    fun progress(): Float = component.props.progress

    @TwineFunction
    fun progress(value: Float): LuaUIProgressBar {
        component.props.progress = value
        return this
    }

    @TwineFunction
    fun fillColor(): LuaColorInstance =
        LuaColor.fromUIColor(component.props.fillColor ?: UIColor.BLACK)

    @TwineFunction
    fun fillColor(value: LuaColorInstance): LuaUIProgressBar {
        component.props.fillColor = value.toUIColor()
        return this
    }

    @TwineFunction
    fun emptyColor(): LuaColorInstance =
        LuaColor.fromUIColor(component.props.emptyColor ?: UIColor.BLACK)

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