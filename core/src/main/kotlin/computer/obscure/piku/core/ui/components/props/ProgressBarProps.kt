package computer.obscure.piku.core.ui.components.props

import computer.obscure.piku.core.ui.classes.DirtyFlag
import computer.obscure.piku.core.ui.classes.FillDirection
import computer.obscure.piku.core.ui.classes.UIColor

open class ProgressBarProps : BaseProps() {

    var progress: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var fillColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var emptyColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }

    var fillDirection: FillDirection? = null
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }
}
