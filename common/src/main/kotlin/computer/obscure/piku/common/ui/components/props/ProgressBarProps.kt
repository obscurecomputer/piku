package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.ui.classes.FillDirection
import computer.obscure.piku.common.ui.classes.UIColor

open class ProgressBarProps(
    var progress: Float = 0f,
    var fillColor: UIColor? = null,
    var emptyColor: UIColor? = null
) : BaseProps()