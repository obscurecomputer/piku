package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.UIColor

open class LineProps(
    var from: Vec2 = Vec2(0f, 0f),
    var to: Vec2 = Vec2(0f, 0f),
    var color: UIColor = UIColor(255, 255, 255),
    var pointSize: Vec2 = Vec2(2f, 2f)
) : BaseProps()