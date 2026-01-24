package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaColorInstance
import computer.obscure.piku.common.scripting.api.LuaVec2
import computer.obscure.piku.common.scripting.api.LuaVec2Instance
import computer.obscure.piku.common.scripting.api.LuaText
import computer.obscure.piku.common.scripting.api.LuaTextInstance
import computer.obscure.piku.common.ui.classes.UIColor
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.ui.components.Text
import computer.obscure.twine.annotations.TwineNativeFunction

class LuaUIText(
    override val component: Text
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var text: LuaTextInstance
        get() = LuaText.fromComponent(component.props.text)
        set(value) {
            component.props.text = value.toComponent()
        }

    @TwineNativeFunction
    fun text(value: LuaTextInstance): LuaUIText {
        component.props.text = value.toComponent()
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