package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.*
import computer.obscure.piku.core.ui.classes.UIColor
import computer.obscure.piku.core.ui.components.Text
import computer.obscure.twine.annotations.TwineFunction

class LuaUIText(
    override val component: Text
) : LuaUIComponent(component) {
    val currentText: LuaTextInstance = LuaTextInstance("text", "")

    @TwineFunction
    fun text(): LuaTextInstance = currentText

    @TwineFunction
    fun text(value: LuaTextInstance): LuaUIText {
        component.props.text = value.toComponent()
        return this
    }

    @TwineFunction
    fun text(value: Any): LuaUIText {
        val instance = LuaTextInstance("text", value.toString())
        component.props.text = instance.toComponent()
        return this
    }

    @TwineFunction
    fun maxWidth(value: Int): LuaUIText {
        component.props.maxWidth = value
        return this
    }

    @TwineFunction
    fun color(): LuaColorInstance = LuaColor.fromUIColor(component.props.color)

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUIText {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineFunction
    fun shadow(): Boolean = component.props.shadow

    @TwineFunction
    fun shadow(value: Boolean): LuaUIText {
        component.props.shadow = value
        return this
    }

    @TwineFunction
    fun backgroundColor(): LuaColorInstance =
        LuaColor.fromUIColor(component.props.backgroundColor ?: UIColor.BLACK)

    @TwineFunction
    fun backgroundColor(value: LuaColorInstance): LuaUIText {
        component.props.backgroundColor = value.toUIColor()
        return this
    }

    @TwineFunction
    fun textScale(): LuaVec2Instance = LuaVec2.fromVec2(component.props.textScale)

    @TwineFunction
    fun textScale(value: LuaVec2Instance): LuaUIText {
        component.props.textScale = value.toVec2()
        return this
    }

    @TwineFunction
    fun backgroundScale(): LuaVec2Instance = LuaVec2.fromVec2(component.props.backgroundScale)

    @TwineFunction
    fun backgroundScale(value: LuaVec2Instance): LuaUIText {
        component.props.backgroundScale = value.toVec2()
        return this
    }
}