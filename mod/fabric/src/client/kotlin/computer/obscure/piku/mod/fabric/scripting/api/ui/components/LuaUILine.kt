package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.twine.annotations.TwineFunction

class LuaUILine (
    override val component: Line
) : LuaUIComponent(component) {
    @TwineFunction
    fun to(): LuaVec2Instance = LuaVec2.fromVec2(component.props.to)

    @TwineFunction
    fun to(value: LuaVec2Instance): LuaUILine {
        component.props.to = value.toVec2()
        return this
    }

    @TwineFunction
    fun from(): LuaVec2Instance = LuaVec2.fromVec2(component.props.from)

    @TwineFunction
    fun from(value: LuaVec2Instance): LuaUILine {
        component.props.from = value.toVec2()
        return this
    }

    @TwineFunction
    fun color(): LuaColorInstance = LuaColor.fromUIColor(component.props.color)

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUILine {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineFunction
    fun pointSize(): LuaVec2Instance = LuaVec2.fromVec2(component.props.pointSize)

    @TwineFunction
    fun pointSize(value: LuaVec2Instance): LuaUILine {
        component.props.pointSize = value.toVec2()
        return this
    }
}