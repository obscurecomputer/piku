package computer.obscure.piku.client.scripting.api.ui.components

import computer.obscure.piku.common.ui.components.Box
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.Gradient
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.piku.common.ui.components.Line
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.piku.common.ui.components.Sprite
import computer.obscure.piku.common.ui.components.Text
import computer.obscure.piku.common.ui.components.props.BoxProps
import computer.obscure.piku.common.ui.components.props.CollectionProps
import computer.obscure.piku.common.ui.components.props.GradientProps
import computer.obscure.piku.common.ui.components.props.LineProps
import computer.obscure.piku.common.ui.components.props.ProgressBarProps
import computer.obscure.piku.common.ui.components.props.SpriteProps
import computer.obscure.piku.common.ui.components.props.TextProps
import computer.obscure.twine.annotations.TwineNativeFunction

open class AllComponentBuilder(
    override val component: Component
) : LuaUIComponent(component) {
    @TwineNativeFunction
    fun group(name: String): LuaUIGroup {
        val newComponent = Group(CollectionProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUIGroup(newComponent)
    }

    @TwineNativeFunction
    fun text(name: String): LuaUIText {
        val newComponent = Text(TextProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUIText(newComponent)
    }

    @TwineNativeFunction
    fun box(name: String): LuaUIBox {
        val newComponent = Box(BoxProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUIBox(newComponent)
    }

    @TwineNativeFunction
    fun sprite(name: String): LuaUISprite {
        val newComponent = Sprite(SpriteProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUISprite(newComponent)
    }

    @TwineNativeFunction
    fun gradient(name: String): LuaUIGradient {
        val newComponent = Gradient(GradientProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUIGradient(newComponent)
    }

    @TwineNativeFunction
    fun progressBar(name: String): LuaUIProgressBar {
        val newComponent = ProgressBar(ProgressBarProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUIProgressBar(newComponent)
    }

    @TwineNativeFunction
    fun line(name: String): LuaUILine {
        val newComponent = Line(LineProps())
        newComponent.name = name
        component.props.components.add(newComponent)
        return LuaUILine(newComponent)
    }
}