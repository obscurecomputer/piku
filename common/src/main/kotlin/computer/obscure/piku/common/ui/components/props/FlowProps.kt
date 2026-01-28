package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.DirtyFlag
import computer.obscure.piku.common.ui.classes.FlowDirection

open class FlowProps : CollectionProps() {

    var gap: Double = 0.0
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }

    var direction: FlowDirection = FlowDirection.VERTICAL
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }
}