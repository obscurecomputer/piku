package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.CollectionProps

data class Group(
    override val props: CollectionProps,
) : Component() {
    override val compType: CompType = CompType.GROUP

    override fun width(): Double {
        val childWidths = props.components.map { it.width() + it.props.pos.x }
        return (childWidths.maxOrNull() ?: 0).toInt() + props.padding.left + props.padding.right
    }

    override fun height(): Double {
        val childHeights = props.components.map { it.height() + it.props.pos.y }
        return (childHeights.maxOrNull() ?: 0).toInt() + props.padding.top + props.padding.bottom
    }
}