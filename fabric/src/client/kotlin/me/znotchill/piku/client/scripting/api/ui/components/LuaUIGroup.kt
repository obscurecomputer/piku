package me.znotchill.piku.client.scripting.api.ui.components

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.nativex.TwineNative
import me.znotchill.piku.client.scripting.api.ui.LuaUI
import me.znotchill.piku.common.ui.components.Box
import me.znotchill.piku.common.ui.components.Group
import me.znotchill.piku.common.ui.components.Sprite
import me.znotchill.piku.common.ui.components.Text
import me.znotchill.piku.common.ui.components.props.BoxProps
import me.znotchill.piku.common.ui.components.props.SpriteProps
import me.znotchill.piku.common.ui.components.props.TextProps

class LuaUIGroup(
    val ui: LuaUI,
    val group: Group
) : LuaUIComponent(
    group
) {
    @TwineNativeFunction("get")
    fun getById(name: String): LuaUIComponent? {
        return ui.smartGet(ui.window.components.values, name)
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