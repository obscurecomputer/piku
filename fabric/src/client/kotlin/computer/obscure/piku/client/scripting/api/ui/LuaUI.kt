package computer.obscure.piku.client.scripting.api.ui

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.client.scripting.api.ui.components.LuaUIBox
import computer.obscure.piku.client.scripting.api.ui.components.LuaUIComponent
import computer.obscure.piku.client.scripting.api.ui.components.LuaUIGradient
import computer.obscure.piku.client.scripting.api.ui.components.LuaUIGroup
import computer.obscure.piku.client.scripting.api.ui.components.LuaUILine
import computer.obscure.piku.client.scripting.api.ui.components.LuaUIProgressBar
import computer.obscure.piku.client.scripting.api.ui.components.LuaUISprite
import computer.obscure.piku.client.scripting.api.ui.components.LuaUIText
import computer.obscure.piku.client.ui.UIRenderer
import computer.obscure.piku.common.ui.components.Box
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.Gradient
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.piku.common.ui.components.Line
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.piku.common.ui.components.Sprite
import computer.obscure.piku.common.ui.components.Text
import computer.obscure.piku.common.ui.components.props.CollectionProps

class LuaUI : TwineNative() {
    val window = UIRenderer.currentWindow

    @TwineNativeFunction
    fun layout() {
        UIRenderer.layout(window)
    }

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
            is Gradient -> LuaUIGradient(component)
            is ProgressBar -> LuaUIProgressBar(component)
            is Line -> LuaUILine(component)
            else -> null
        }
}