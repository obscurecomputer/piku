package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.DirtyFlag
import computer.obscure.piku.common.ui.classes.FillDirection
import computer.obscure.piku.common.ui.classes.UIColor

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
}
