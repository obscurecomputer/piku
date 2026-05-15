package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.ui.components.SpriteNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUISprite(override val node: SpriteNode) : LuaUIContainer(node) {
    @TwineFunction
    fun texture(value: String): LuaUISprite {
        node.texturePath = value
        return this
    }
}