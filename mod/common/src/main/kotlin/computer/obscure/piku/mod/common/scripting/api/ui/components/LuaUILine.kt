package computer.obscure.piku.mod.common.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty

class LuaUILine (
    override val component: Line
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var to: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.to)
        set(value) {
            component.props.to = value.toVec2()
        }

    @TwineNativeFunction
    fun to(value: LuaVec2Instance): LuaUILine {
        component.props.to = value.toVec2()
        return this
    }

    @TwineNativeProperty
    var from: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.from)
        set(value) {
            component.props.to = value.toVec2()
        }

    @TwineNativeFunction
    fun from(value: LuaVec2Instance): LuaUILine {
        component.props.from = value.toVec2()
        return this
    }

    @TwineNativeProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }

    @TwineNativeFunction
    fun color(value: LuaColorInstance): LuaUILine {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineNativeProperty
    var pointSize: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.pointSize)
        set(value) {
            component.props.pointSize = value.toVec2()
        }

    @TwineNativeFunction
    fun pointSize(value: LuaVec2Instance): LuaUILine {
        component.props.pointSize = value.toVec2()
        return this
    }
}