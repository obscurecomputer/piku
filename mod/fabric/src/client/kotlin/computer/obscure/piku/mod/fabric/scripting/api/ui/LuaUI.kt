package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import computer.obscure.piku.core.ui.components.Box
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.FlowContainer
import computer.obscure.piku.core.ui.components.Gradient
import computer.obscure.piku.core.ui.components.Group
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.piku.core.ui.components.Text
import computer.obscure.piku.core.ui.components.props.CollectionProps
import computer.obscure.piku.core.ui.components.props.FlowProps
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIBox
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIComponent
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIFlow
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIGradient
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIGroup
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUILine
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIProgressBar
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUISprite
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIText
import computer.obscure.piku.mod.fabric.ui.UIRenderer

class LuaUI : TwineNative() {
    val window = UIRenderer.currentWindow

    @TwineFunction
    fun layout() {
        UIRenderer.layout(window)
    }

    @TwineFunction
    fun group(name: String = ""): LuaUIGroup {
        val component = Group(
            CollectionProps()
        )
        component.name = name
        window.add(component)

        return LuaUIGroup(
            component
        )
    }

    @TwineFunction
    fun flow(name: String = ""): LuaUIFlow {
        val component = FlowContainer(
            FlowProps()
        )
        component.name = name
        window.add(component)

        return LuaUIFlow(
            component
        )
    }

    @TwineFunction
    fun clear() {
        window.components.clear()
    }

    // cannot be called "get" since it overrides LuaValue's get method
    @TwineFunction("get")
    fun getById(name: String): LuaUIComponent? {
        return smartGet(window.components.values, name)
    }

    @TwineFunction("exists")
    fun exists(name: String): Boolean {
        return smartGet(window.components.values, name) != null
    }

    @TwineFunction()
    fun debug(value: Boolean) {
        UIRenderer.debugEnabled = value
    }

    companion object {
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

        fun wrap(component: Component): LuaUIComponent? =
            when (component) {
                is Text -> LuaUIText(component)
                is Group -> LuaUIGroup(component)
                is Box -> LuaUIBox(component)
                is Sprite -> LuaUISprite(component)
                is Gradient -> LuaUIGradient(component)
                is ProgressBar -> LuaUIProgressBar(component)
                is Line -> LuaUILine(component)
                is FlowContainer -> LuaUIFlow(component)
                else -> null
            }
    }
}