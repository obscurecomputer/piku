package me.znotchill.piku.common.ui.components.props

import me.znotchill.piku.common.ui.classes.UIColor

open class GradientProps(
    var from: UIColor = UIColor(0, 0, 0),
    var to: UIColor = UIColor(255, 255, 255),
    var fillScreen: Boolean = false
) : BaseProps()