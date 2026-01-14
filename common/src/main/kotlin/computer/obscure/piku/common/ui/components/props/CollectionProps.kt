package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.DirtyFlag
import computer.obscure.piku.common.ui.classes.UIColor
import computer.obscure.piku.common.ui.components.Component

open class CollectionProps : BaseProps() {

    var components: MutableList<Component> = mutableListOf()
        set(value) {
            field = value
            mark(DirtyFlag.LAYOUT)
        }

    var backgroundColor: UIColor? = null
        set(value) {
            if (field != value) {
                field = value
                mark(DirtyFlag.VISUAL)
            }
        }
}