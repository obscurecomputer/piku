package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.SpriteProps

open class Sprite(
    override val props: SpriteProps
) : Component() {
    override val compType: CompType
        get() = CompType.SPRITE

    override fun width(): Double {
        val drawWidth = if (props.size.x > 0)
            props.size.x else (computedSize?.x ?: 0)
        val baseWidth = drawWidth.toDouble() + props.padding.left + props.padding.right
        return (baseWidth * props.scale.x)
    }

    override fun height(): Double {
        val drawHeight = if (props.size.y > 0) props.size.y else (computedSize?.y ?: 0f)
        val baseHeight = drawHeight.toDouble() + props.padding.top + props.padding.bottom
        return (baseHeight * props.scale.y)
    }
}