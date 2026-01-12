package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.FlowDirection

open class FlowProps(
    var gap: Double = 0.0,
    var direction: FlowDirection = FlowDirection.HORIZONTAL
) : CollectionProps()