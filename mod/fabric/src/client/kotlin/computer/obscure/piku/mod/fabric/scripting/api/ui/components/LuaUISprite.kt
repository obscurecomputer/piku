package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.twine.annotations.TwineFunction

class LuaUISprite(
    override val component: Sprite
) : LuaUIComponent(component) {
    @TwineFunction
    fun color(): LuaColorInstance {
        return LuaColor.fromUIColor(component.props.color)
    }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUISprite {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineFunction
    fun texture(): String = component.props.texturePath

    @TwineFunction
    fun texture(value: String): LuaUISprite {
        component.props.texturePath = value
        return this
    }

    @TwineFunction
    fun fillScreen(): Boolean = component.props.fillScreen

    @TwineFunction
    fun fillScreen(value: Boolean): LuaUISprite {
        component.props.fillScreen = value
        return this
    }

    @TwineFunction
    fun tiled(value: Boolean): LuaUISprite {
        component.props.tiled = value
        return this
    }

    @TwineFunction
    fun backgroundColor(value: LuaColorInstance): LuaUISprite {
        component.props.backgroundColor = value.toUIColor()
        return this
    }
}