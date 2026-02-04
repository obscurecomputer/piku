package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.CollectionProps

data class Group(
    override val props: CollectionProps,
) : Component() {
    override val compType: CompType = CompType.GROUP

    override fun width(): Double {
        val maxChildEdge = props.components.maxOfOrNull { it.props.pos.x + it.width() } ?: 0.0
        return maxChildEdge + props.padding.left + props.padding.right
    }

    override fun height(): Double {
        val maxChildEdge = props.components.maxOfOrNull { it.props.pos.y + it.height() } ?: 0.0
        return maxChildEdge + props.padding.top + props.padding.bottom
    }
}