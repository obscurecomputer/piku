package computer.obscure.piku.common.ui.components.props

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.UIColor

open class TextProps(
    var text: String = "",
    var color: UIColor = UIColor(255, 255, 255),
    var shadow: Boolean = false,
    var backgroundColor: UIColor? = null,
    var textScale: Vec2 = Vec2(1f, 1f),
    var backgroundScale: Vec2 = Vec2(1f, 1f)
) : BaseProps()