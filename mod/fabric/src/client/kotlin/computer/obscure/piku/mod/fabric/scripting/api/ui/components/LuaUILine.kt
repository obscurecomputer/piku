package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaUILine (
    override val component: Line
) : LuaUIComponent(component) {
    @TwineProperty
    var to: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.to)
        set(value) {
            component.props.to = value.toVec2()
        }

    @TwineFunction
    fun to(value: LuaVec2Instance): LuaUILine {
        component.props.to = value.toVec2()
        return this
    }

    @TwineProperty
    var from: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.from)
        set(value) {
            component.props.to = value.toVec2()
        }

    @TwineFunction
    fun from(value: LuaVec2Instance): LuaUILine {
        component.props.from = value.toVec2()
        return this
    }

    @TwineProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUILine {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineProperty
    var pointSize: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.pointSize)
        set(value) {
            component.props.pointSize = value.toVec2()
        }

    @TwineFunction
    fun pointSize(value: LuaVec2Instance): LuaUILine {
        component.props.pointSize = value.toVec2()
        return this
    }
}