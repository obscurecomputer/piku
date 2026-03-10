package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.components.props.SpriteProps

open class Sprite(
    override val props: SpriteProps
) : Component() {
    override val compType: CompType
        get() = CompType.SPRITE

    var textureResolved = false

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