package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.ui.components.Sprite

class LuaUISprite(
    override val component: Sprite
) : LuaUIComponent(component) {
    @TwineNativeProperty
    var texture: String
        get() = component.props.texturePath
        set(value) {
            component.props.texturePath = value
        }
}