package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.piku.client.scripting.api.ui.LuaUI
import computer.obscure.piku.common.ui.components.Box
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.piku.common.ui.components.Sprite
import computer.obscure.piku.common.ui.components.Text
import computer.obscure.piku.common.ui.components.props.BoxProps
import computer.obscure.piku.common.ui.components.props.SpriteProps
import computer.obscure.piku.common.ui.components.props.TextProps

class LuaUIGroup(
    val ui: LuaUI,
    val group: Group
) : LuaUIComponent(
    group
) {
    @TwineNativeFunction("get")
    fun getById(name: String): LuaUIComponent? {
        return ui.smartGet(group.props.components, name)
    }

    @TwineNativeFunction
    fun text(name: String): LuaUIText {
        val component = Text(TextProps())
        component.name = name
        group.props.components.add(component)
        return LuaUIText(component)
    }

    @TwineNativeFunction
    fun box(name: String): LuaUIBox {
        val component = Box(BoxProps())
        component.name = name
        group.props.components.add(component)
        return LuaUIBox(component)
    }

    @TwineNativeFunction
    fun sprite(name: String): LuaUISprite {
        val component = Sprite(SpriteProps())
        component.name = name
        group.props.components.add(component)
        return LuaUISprite(component)
    }
}