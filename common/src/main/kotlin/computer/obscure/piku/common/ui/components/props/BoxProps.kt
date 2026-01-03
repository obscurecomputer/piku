package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.UIColor

open class BoxProps(
    var color: UIColor = UIColor(255, 255, 255),
    var fillScreen: Boolean = false
) : BaseProps(
    size = Vec2(0f, 0f),
    pos = Vec2(0f, 0f)
)