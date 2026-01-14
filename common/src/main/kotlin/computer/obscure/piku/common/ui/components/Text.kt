package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.TextProps
import computer.obscure.piku.common.ui.mcWidth

open class Text(
    override val props: TextProps
) : Component() {
    override val compType: CompType = CompType.TEXT

    var cachedTextSize: Vec2? = null
    var cachedText: String? = null

    override fun width(): Double {
        val computed = computedSize?.x ?: 0.0
        if (computed > 0.0) return computed

        val lines = props.text.split("\n")
        val widest = lines.maxOfOrNull { it.mcWidth() } ?: 0
        val baseWidth = widest + props.padding.left + props.padding.right
        return (baseWidth * props.scale.x)
    }

    override fun height(): Double {
        val computed = computedSize?.y ?: 0.0
        if (computed > 0.0) return computed

        val lineCount = props.text.count { it == '\n' } + 1
        val baseHeight = (7 * lineCount) + props.padding.top + props.padding.bottom
        return (baseHeight * props.scale.y)
    }
}