package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.FlowDirection

open class FlowProps(
    var gap: Float = 0f,
    var direction: FlowDirection = FlowDirection.HORIZONTAL
) : CollectionProps()