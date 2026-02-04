package computer.obscure.piku.mod.common.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.twine.annotations.TwineNativeFunction

class LuaUISprite(
    override val component: Sprite
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var texture: String
        get() = component.props.texturePath
        set(value) {
            component.props.texturePath = value
        }

    @TwineNativeFunction
    fun texture(value: String): LuaUISprite {
        component.props.texturePath = value
        return this
    }
}