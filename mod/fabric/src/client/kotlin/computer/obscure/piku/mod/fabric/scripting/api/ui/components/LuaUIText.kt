package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.core.ui.classes.UIColor
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.piku.core.ui.components.Text
import computer.obscure.twine.annotations.TwineFunction

class LuaUIText(
    override val component: Text
) : LuaUIComponent(component) {

    @TwineFunction
    fun text(value: LuaTextInstance): LuaUIText {
        component.props.text = value.toComponent()
        return this
    }

    @TwineFunction
    fun text(value: Any): LuaUIText {
        component.props.text = LuaTextInstance("text", value.toString()).toComponent()
        return this
    }

    @TwineProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUIText {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineProperty
    var shadow: Boolean
        get() = component.props.shadow
        set(value) {
            component.props.shadow = value
        }

    @TwineFunction
    fun shadow(value: Boolean): LuaUIText {
        component.props.shadow = value
        return this
    }

    @TwineProperty
    var backgroundColor: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.backgroundColor ?: UIColor.BLACK)
        set(value) {
            component.props.backgroundColor = value.toUIColor()
        }

    @TwineFunction
    fun backgroundColor(value: LuaColorInstance): LuaUIText {
        component.props.backgroundColor = value.toUIColor()
        return this
    }

    @TwineProperty
    var textScale: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.textScale)
        set(value) {
            component.props.textScale = value.toVec2()
        }

    @TwineFunction
    fun textScale(value: LuaVec2Instance): LuaUIText {
        component.props.textScale = value.toVec2()
        return this
    }

    @TwineProperty
    var backgroundScale: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.backgroundScale)
        set(value) {
            component.props.backgroundScale = value.toVec2()
        }

    @TwineFunction
    fun backgroundScale(value: LuaVec2Instance): LuaUIText {
        component.props.backgroundScale = value.toVec2()
        return this
    }
}