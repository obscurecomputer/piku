package me.znotchill.piku.client.scripting.api.ui

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import me.znotchill.piku.client.scripting.api.ui.components.LuaUIBox
import me.znotchill.piku.client.scripting.api.ui.components.LuaUIComponent
import me.znotchill.piku.client.scripting.api.ui.components.LuaUIGroup
import me.znotchill.piku.client.scripting.api.ui.components.LuaUISprite
import me.znotchill.piku.client.scripting.api.ui.components.LuaUIText
import me.znotchill.piku.client.ui.UIRenderer
import me.znotchill.piku.common.ui.components.Box
import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.components.Group
import me.znotchill.piku.common.ui.components.Sprite
import me.znotchill.piku.common.ui.components.Text
import me.znotchill.piku.common.ui.components.props.CollectionProps

class LuaUI : TwineNative() {

    val window = UIRenderer.currentWindow

    @TwineNativeFunction
    fun group(name: String = ""): LuaUIGroup {
        val component = Group(
            CollectionProps()
        )
        component.name = name
        window.add(component)

        return LuaUIGroup(
            this, component
        )
    }

    @TwineNativeFunction
    fun clear() {
        window.components.clear()
    }

    // cannot be called "get" since it overrides LuaValue's get method
    @TwineNativeFunction("get")
    fun getById(name: String): LuaUIComponent? {
        return smartGet(window.components.values, name)
    }

    fun smartGet(components: Collection<Component>, name: String): LuaUIComponent? {
        for (component in components) {
            if (component.name == name) {
                return wrap(component)
            }

            // recurse
            if (component is Group) {
                smartGet(component.props.components, name)?.let {
                    return it
                }
            }
        }
        return null
    }

    private fun wrap(component: Component): LuaUIComponent? =
        when (component) {
            is Text -> LuaUIText(component)
            is Group -> LuaUIGroup(this, component)
            is Box -> LuaUIBox(component)
            is Sprite -> LuaUISprite(component)
            else -> null
        }
}