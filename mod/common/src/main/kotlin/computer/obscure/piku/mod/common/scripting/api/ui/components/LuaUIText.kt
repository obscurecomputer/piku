package computer.obscure.piku.mod.common.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.core.ui.classes.UIColor
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.core.ui.components.Text
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineOverload

class LuaUIText(
    override val component: Text
) : LuaUIComponent(component) {

    @TwineOverload
    @TwineNativeFunction
    fun text(value: LuaTextInstance): LuaUIText {
        component.props.text = value.toComponent()
        return this
    }

    @TwineOverload
    @TwineNativeFunction
    fun text(value: Any): LuaUIText {
        component.props.text = LuaTextInstance("text", value.toString()).toComponent()
        return this
    }

    @TwineNativeProperty
    var color: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.color)
        set(value) {
            component.props.color = value.toUIColor()
        }

    @TwineNativeFunction
    fun color(value: LuaColorInstance): LuaUIText {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineNativeProperty
    var shadow: Boolean
        get() = component.props.shadow
        set(value) {
            component.props.shadow = value
        }

    @TwineNativeFunction
    fun shadow(value: Boolean): LuaUIText {
        component.props.shadow = value
        return this
    }

    @TwineNativeProperty
    var backgroundColor: LuaColorInstance
        get() = LuaColor.fromUIColor(component.props.backgroundColor ?: UIColor.BLACK)
        set(value) {
            component.props.backgroundColor = value.toUIColor()
        }

    @TwineNativeFunction
    fun backgroundColor(value: LuaColorInstance): LuaUIText {
        component.props.backgroundColor = value.toUIColor()
        return this
    }

    @TwineNativeProperty
    var textScale: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.textScale)
        set(value) {
            component.props.textScale = value.toVec2()
        }

    @TwineNativeFunction
    fun textScale(value: LuaVec2Instance): LuaUIText {
        component.props.textScale = value.toVec2()
        return this
    }

    @TwineNativeProperty
    var backgroundScale: LuaVec2Instance
        get() = LuaVec2.fromVec2(component.props.backgroundScale)
        set(value) {
            component.props.backgroundScale = value.toVec2()
        }

    @TwineNativeFunction
    fun backgroundScale(value: LuaVec2Instance): LuaUIText {
        component.props.backgroundScale = value.toVec2()
        return this
    }
}