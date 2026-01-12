package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.classes.FlowDirection
import computer.obscure.piku.common.ui.components.props.FlowProps

data class FlowContainer(
    override val props: FlowProps,
) : Component() {
    override val compType: CompType = CompType.FLOW_CONTAINER

    override fun width(): Double {
        val totalWidth = when (props.direction) {
            FlowDirection.HORIZONTAL -> {
                val contentWidth = props.components.sumOf { (it.width() + it.props.margin.left + it.props.margin.right) } +
                        ((props.components.size - 1) * props.gap)
                (contentWidth + props.padding.left + props.padding.right)
            }
            FlowDirection.VERTICAL -> {
                val contentWidth = props.components.maxOfOrNull {
                    it.width() + it.props.margin.left + it.props.margin.right
                } ?: 0.0
                contentWidth + props.padding.left + props.padding.right
            }
        }
        return totalWidth
    }

    override fun height(): Double {
        val totalHeight = when (props.direction) {
            FlowDirection.VERTICAL -> {
                val contentHeight = props.components.sumOf { (it.height() + it.props.margin.top + it.props.margin.bottom) } +
                        ((props.components.size - 1) * props.gap)
                (contentHeight + props.padding.top + props.padding.bottom)
            }
            FlowDirection.HORIZONTAL -> {
                val contentHeight = props.components.maxOfOrNull {
                    it.height() + it.props.margin.top + it.props.margin.bottom
                } ?: 0.0
                contentHeight + props.padding.top + props.padding.bottom
            }
        }
        return totalHeight
    }
}
