package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.UIColor
import computer.obscure.piku.common.ui.components.Component

open class CollectionProps(
    var components: MutableList<Component> = mutableListOf(),
    var backgroundColor: UIColor? = null
) : BaseProps()