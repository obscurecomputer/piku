package me.znotchill.piku.common.ui.components.props

import me.znotchill.piku.common.ui.classes.UIColor
import me.znotchill.piku.common.ui.components.Component

open class CollectionProps(
    var components: MutableList<Component> = mutableListOf(),
    var backgroundColor: UIColor? = null
) : BaseProps()