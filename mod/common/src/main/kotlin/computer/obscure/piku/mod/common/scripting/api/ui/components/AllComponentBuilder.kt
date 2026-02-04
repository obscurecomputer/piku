package computer.obscure.piku.mod.common.scripting.api.ui.components

import computer.obscure.piku.core.ui.components.Box
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.FlowContainer
import computer.obscure.piku.core.ui.components.Gradient
import computer.obscure.piku.core.ui.components.Group
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.piku.core.ui.components.Text
import computer.obscure.piku.core.ui.components.props.BoxProps
import computer.obscure.piku.core.ui.components.props.CollectionProps
import computer.obscure.piku.core.ui.components.props.FlowProps
import computer.obscure.piku.core.ui.components.props.GradientProps
import computer.obscure.piku.core.ui.components.props.LineProps
import computer.obscure.piku.core.ui.components.props.ProgressBarProps
import computer.obscure.piku.core.ui.components.props.SpriteProps
import computer.obscure.piku.core.ui.components.props.TextProps
import computer.obscure.piku.mod.common.ui.UIRenderer
import computer.obscure.twine.annotations.TwineNativeFunction

open class AllComponentBuilder(
    override val component: Component
) : LuaUIComponent(component) {

    private fun <T : Component> setup(newComponent: T, name: String): T {
        newComponent.name = name
        component.props.components.add(newComponent)
        UIRenderer.currentWindow.registerRecursive(newComponent)
        return newComponent
    }

    @TwineNativeFunction
    fun group(name: String): LuaUIGroup = LuaUIGroup(setup(Group(CollectionProps()), name))

    @TwineNativeFunction
    fun text(name: String): LuaUIText = LuaUIText(setup(Text(TextProps()), name))

    @TwineNativeFunction
    fun box(name: String): LuaUIBox = LuaUIBox(setup(Box(BoxProps()), name))

    @TwineNativeFunction
    fun sprite(name: String): LuaUISprite = LuaUISprite(setup(Sprite(SpriteProps()), name))

    @TwineNativeFunction
    fun gradient(name: String): LuaUIGradient = LuaUIGradient(setup(Gradient(GradientProps()), name))

    @TwineNativeFunction
    fun progressBar(name: String): LuaUIProgressBar = LuaUIProgressBar(setup(ProgressBar(ProgressBarProps()), name))

    @TwineNativeFunction
    fun line(name: String): LuaUILine = LuaUILine(setup(Line(LineProps()), name))

    @TwineNativeFunction
    fun flow(name: String): LuaUIFlow {
        val newComponent = setup(FlowContainer(FlowProps()), name)
        val wrapper = LuaUIFlow(newComponent)
        return wrapper
    }
}