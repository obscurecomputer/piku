package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.twine.annotations.TwineFunction

class LuaUISprite(
    override val component: Sprite
) : LuaUIComponent(component) {
    @TwineProperty
    var texture: String
        get() = component.props.texturePath
        set(value) {
            component.props.texturePath = value
        }

    @TwineFunction
    fun texture(value: String): LuaUISprite {
        component.props.texturePath = value
        return this
    }

    @TwineFunction
    fun color(value: LuaColorInstance): LuaUISprite {
        component.props.color = value.toUIColor()
        return this
    }

    @TwineProperty
    var fillScreen: Boolean
        get() = component.props.fillScreen
        set(value) {
            component.props.fillScreen = value
        }

    @TwineFunction
    fun fillScreen(): LuaUIComponent {
        component.props.fillScreen = true
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