package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.components.props.FlowProps

data class FlowContainer(
    override val props: FlowProps,
) : Component() {
    override val compType: CompType = CompType.FLOW_CONTAINER

    override fun width(): Double {
        if (props.size.x > 0) return props.size.x
        return computedSize?.x ?: 0.0
    }

    override fun height(): Double {
        if (props.size.y > 0) return props.size.y
        return computedSize?.y ?: 0.0
    }
}
