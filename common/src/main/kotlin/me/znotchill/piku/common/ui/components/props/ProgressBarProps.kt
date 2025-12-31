package me.znotchill.piku.common.ui.components.props

import me.znotchill.piku.common.ui.classes.FillDirection
import me.znotchill.piku.common.ui.classes.UIColor

open class ProgressBarProps(
    var progress: Float = 0f,
    var fillColor: UIColor? = null,
    var emptyColor: UIColor? = null,
    var fillDirection: FillDirection = FillDirection.RIGHT
) : BaseProps()