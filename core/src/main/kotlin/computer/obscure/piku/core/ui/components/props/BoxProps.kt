package computer.obscure.piku.core.ui.components.props

import computer.obscure.piku.core.ui.classes.DirtyFlag
import computer.obscure.piku.core.ui.classes.UIColor

open class BoxProps : BaseProps() {
    var color: UIColor = UIColor(255, 255, 255)
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var fillScreen: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.LAYOUT)
            }
        }
}
