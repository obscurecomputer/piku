package computer.obscure.piku.core.ui.components.props

import computer.obscure.piku.core.ui.classes.DirtyFlag
import computer.obscure.piku.core.ui.classes.UIColor

open class CollectionProps : BaseProps() {

    var backgroundColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }
}